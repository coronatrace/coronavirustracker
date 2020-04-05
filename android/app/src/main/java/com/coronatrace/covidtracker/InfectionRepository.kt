package com.coronatrace.covidtracker

import androidx.lifecycle.LiveData

class InfectionRepository(private val infectionDao: InfectionDao) {
    val allInfections: LiveData<List<Infection>> = infectionDao.getInfections()

    suspend fun insertInfection(infection: Infection) {
        infectionDao.insert(infection)
    }

    suspend fun updateInfection(infection: Infection) {
        infectionDao.update(infection)
    }
}