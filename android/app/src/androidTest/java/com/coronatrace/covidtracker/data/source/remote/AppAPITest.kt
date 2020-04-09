package com.coronatrace.covidtracker.data.source.remote

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.DetectionSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AppAPITest {

    var api: AppAPI? = null

    companion object {
        @BeforeClass  @JvmStatic
        fun initAmplify() {
            val context = ApplicationProvider.getApplicationContext<android.content.Context>()
            Amplify.addPlugin(AWSApiPlugin())
            Amplify.configure(context)
        }
    }

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        api = AppAPI(context)
    }

    @Test
    fun addContact() {
        runBlocking {
            api?.addContact(System.currentTimeMillis(), "random-user-id-123")
        }
    }

    @Test
    fun addInfection() {
        runBlocking {
            api?.addInfection(System.currentTimeMillis(), DetectionSource.symptoms)
        }
    }

    @Test
    fun getInfections() {
        runBlocking {
            Log.d("Amplify", "hi1")
            val res = api?.getInfections("eu-west-1:dbec7b3b-8a92-4543-82be-c0d589637db1")
            Log.d("Amplify", "hi $res")
        }

    }

}