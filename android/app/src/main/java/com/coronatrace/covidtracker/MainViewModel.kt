package com.coronatrace.covidtracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InfectionRepository

    val allInfections: LiveData<List<Infection>>

    init {
        val infectionDao = AppRoomDatabase.getDatabase(application).infectionDao()
        repository = InfectionRepository(infectionDao)
        allInfections = repository.allInfections
    }

    fun insert(infection: Infection) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertInfection(infection)
    }

    fun update(infection: Infection) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateInfection(infection)
    }

}