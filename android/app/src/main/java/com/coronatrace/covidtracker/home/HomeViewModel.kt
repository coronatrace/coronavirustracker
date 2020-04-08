package com.coronatrace.covidtracker.home

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    val trackingEnabled = MutableLiveData<Boolean>(true)

    private val trackingPreferences = application.getSharedPreferences(null, MODE_PRIVATE)

    init {
        // Default to all-clear
        setStatusNoContact()

        // Get trackingEnabled preferences
        val trackingEnabledInt = trackingPreferences.getInt("trackingEnabled", 0)
        trackingEnabled.value = trackingEnabledInt == 1

        // Setup repository
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

    fun handleTrackingEnabledSwitch() {
        val oppositeValue = trackingEnabled.value != true
        trackingEnabled.value = oppositeValue

        val trackingEnabledInt = if (oppositeValue) 1 else 0
        trackingPreferences.edit().putInt("trackingEnabled", trackingEnabledInt ).apply()
    }


}