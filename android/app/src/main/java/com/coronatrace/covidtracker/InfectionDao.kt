package com.coronatrace.covidtracker

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InfectionDao {

    @Query("SELECT * from infections ORDER BY 'internalId' DESC")
    fun getInfections(): LiveData<List<Infection>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(infection: Infection)

    @Update
    suspend fun update(infection: Infection)

}