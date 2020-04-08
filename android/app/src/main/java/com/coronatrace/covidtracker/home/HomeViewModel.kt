package com.coronatrace.covidtracker.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.coronatrace.covidtracker.data.Infection
import com.coronatrace.covidtracker.data.source.InfectionRepository
import com.coronatrace.covidtracker.data.source.local.AppRoomDatabase
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InfectionRepository

    val latestInfection: LiveData<Infection>

    init {
        val infectionDao = AppRoomDatabase.getDatabase(application).infectionDao()
        repository =
            InfectionRepository(
                infectionDao
            )
        latestInfection = repository.latestInfection
    }

    fun resetInfectionsFromSymptoms() = viewModelScope.launch {
        repository.resetInfectionsFromSymptoms()
    }

}