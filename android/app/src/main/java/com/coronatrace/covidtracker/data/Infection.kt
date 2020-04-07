package com.coronatrace.covidtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infection")
data class Infection(
    @PrimaryKey(autoGenerate = true) val localId: Int?,
    val remoteId: String?,
    val timestamp: Long, // Note for infection via contact this is the time of notification, not infection
    val source: String, // E.g. test, contact...
    val infectedStatus: Boolean // True means infected, false is e.g. a test proving not infected
)