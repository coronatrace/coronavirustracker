package com.coronatrace.covidtracker.data.source

import androidx.lifecycle.LiveData
import com.coronatrace.covidtracker.data.source.local.InfectionDao
import com.coronatrace.covidtracker.data.Infection

class InfectionRepository(private val infectionDao: InfectionDao) {
    val latestInfection: LiveData<Infection> = infectionDao.getLatestInfection()

    suspend fun insertInfection(infection: Infection) {
        infectionDao.insert(infection)
    }

    suspend fun resetInfectionsFromSymptoms() {
        infectionDao.resetInfectionsFromSymptoms()
    }

}