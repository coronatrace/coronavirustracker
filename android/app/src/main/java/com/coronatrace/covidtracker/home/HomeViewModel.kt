package com.coronatrace.covidtracker.home

import android.app.Application
import android.database.Observable
import android.opengl.Visibility
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.data.Infection
import com.coronatrace.covidtracker.data.source.InfectionRepository
import com.coronatrace.covidtracker.data.source.local.AppRoomDatabase
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InfectionRepository

    val latestInfection: LiveData<Infection>
    var statusText = ObservableField<Int>()
    var statusBody = ObservableField<Int>()
    var resetButtonVisibility = ObservableField<Int>()
    val cardBackgroundColor = ObservableField<Int>()
    val cardTextColor = ObservableField<Int>()

    init {
        setStatusNoContact()
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

    fun setStatusSymptoms() {
        statusText.set(R.string.status_symptoms_title)
        statusBody.set(R.string.status_symptoms_body)
        resetButtonVisibility.set(View.VISIBLE)
        cardBackgroundColor.set(R.color.error)
        cardTextColor.set(R.color.onError)
    }

    fun setStatusNoContact() {
        statusText.set(R.string.status_no_contact_title)
        statusBody.set(R.string.status_no_contact_description)
        resetButtonVisibility.set(View.GONE)
        cardBackgroundColor.set(R.color.lightGray)
        cardTextColor.set(R.color.onBackground)
    }

}