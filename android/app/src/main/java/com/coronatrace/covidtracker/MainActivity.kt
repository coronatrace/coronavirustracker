package com.coronatrace.covidtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.core.Amplify


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure AWS Amplify
        Amplify.addPlugin(AWSApiPlugin())
        Amplify.configure(applicationContext)

        setContentView(R.layout.activity_main)
    }

}
