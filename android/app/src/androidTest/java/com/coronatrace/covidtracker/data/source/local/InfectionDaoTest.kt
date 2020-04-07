package com.coronatrace.covidtracker.data.source.local

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.coronatrace.covidtracker.data.Infection
import com.coronatrace.covidtracker.utils.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class InfectionDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var infectionDao: InfectionDao
    private lateinit var db: AppRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppRoomDatabase::class.java).build()
        infectionDao = db.infectionDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertInfection() {
        val infection = Infection(null, null, System.currentTimeMillis(), "symptoms", true)
        runBlocking { infectionDao.insert(infection) }
        val insertedItem = infectionDao.getLatestInfection().getOrAwaitValue()

        assertThat(insertedItem.source, `is`(infection.source))
    }

    @Test
    fun getLatestInfection() {
        val infection1 = Infection(null,"abc", System.currentTimeMillis(), "symptoms", true)
        val infection2 = Infection(null,"def", System.currentTimeMillis(), "symptoms", true)
        runBlocking { infectionDao.insert(infection1) }
        runBlocking { infectionDao.insert(infection2) }
        val latestItem = infectionDao.getLatestInfection().getOrAwaitValue()

        assertThat(latestItem.remoteId, `is`(infection2.remoteId))
    }

}
