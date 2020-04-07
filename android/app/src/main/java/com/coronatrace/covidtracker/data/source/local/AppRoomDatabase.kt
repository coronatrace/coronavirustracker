package com.coronatrace.covidtracker.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coronatrace.covidtracker.data.Infection

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Infection::class], version = 1, exportSchema = false)
public abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun infectionDao(): InfectionDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}