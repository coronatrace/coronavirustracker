package com.coronatrace.covidtracker.logcontact

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.amplifyframework.api.graphql.MutationType;
import com.amplifyframework.core.Amplify;
import com.coronatrace.covidtracker.MainActivity
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.auth.Auth
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


class LogContactService : Service() {

    val CHANNEL_ID = "ForegroundServiceChannel"
    private var message: Message? = null
    private var messageListener: MessageListener? = null

    val userIdentityId: String
        get() {
            val id = Auth(this).userIdentityId
            if (id != null) return id else return ""
        }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startInForeground(intent)

        // Setup message listener
        messageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                // Get the contact details
                val contactUserId = String(message.content)
                val contactTimestamp = System.currentTimeMillis()
                Log.d(
                    "Message listener",
                    "Found user $contactUserId with timestamp $contactTimestamp"
                );

                // Push to backend

//                val contact: Contact = Contact.builder().userId(userId).contactUserId(contactUserId)
//                    .contactTimestamp(contactTimestamp).build()
//                Amplify.API.mutate(contact, MutationType.CREATE,
//                    { taskCreationResponse -> },
//                    { apiFailure -> } //Log.e("AmplifyGetStarted", "Failed to create a task.", apiFailure)
//                )
            }
        }

        // Setup the message (just the UUID)
        message = Message(userIdentityId.toByteArray())

        startLoggingScheduler()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopLoggingScheduler()
        stopLogging.run()
        super.onDestroy()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Timers for logging once every minute
     */

    private var startLog: ScheduledFuture<*>? = null
    private var stopLog: ScheduledFuture<*>? = null

    private fun startLoggingScheduler() {
        // Set to start scheduler at the beginning of every UTC minute
        val sdf = SimpleDateFormat("ssSSS")
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
        val secondMillisecondString: String = sdf.format(Date())
        val secondMillisecond = secondMillisecondString.toInt()
        val delay = (60000 - secondMillisecond).toLong()

        // Start at the beginning of every minute
        val scheduler = ScheduledThreadPoolExecutor(2)
        startLog = scheduler.scheduleAtFixedRate(
            startLogging,
            delay,
            60000,
            TimeUnit.MILLISECONDS
        )

        // Run for 15 seconds
        stopLog = scheduler.scheduleAtFixedRate(
            stopLogging,
            (delay + 15000).toLong(),
            60000,
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopLoggingScheduler() {
        startLog?.cancel(true)
        stopLog?.cancel(true)
    }

    private val startLogging = Runnable {
        message?.let { Nearby.getMessagesClient(this).publish(it) }
        messageListener?.let { Nearby.getMessagesClient(this).subscribe(it) }
    }

    private val stopLogging = Runnable {
        message?.let { Nearby.getMessagesClient(this).unpublish(it) }
        messageListener?.let { Nearby.getMessagesClient(this).unsubscribe(it) }
    }

    /**
     * Foreground service (notification)
     */

    private fun startInForeground(intent: Intent) {
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getText(R.string.foreground_notification_title))
            .setContentText(getText(R.string.foreground_notification_body))
            .setSmallIcon(R.drawable.ic_bluetooth_audio_primary_24dp)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}
