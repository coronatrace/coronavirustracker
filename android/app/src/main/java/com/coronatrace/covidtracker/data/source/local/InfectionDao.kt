package com.coronatrace.covidtracker.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.coronatrace.covidtracker.data.Infection

@Dao
interface InfectionDao {

    @Query("SELECT * from infection WHERE cancelled != 1 ORDER BY localId DESC LIMIT 1")
    fun getLatestInfection(): LiveData<Infection>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(infection: Infection)

    @Query("UPDATE infection SET cancelled = 1  WHERE source = 'symptoms'")
    suspend fun resetInfectionsFromSymptoms()

}


