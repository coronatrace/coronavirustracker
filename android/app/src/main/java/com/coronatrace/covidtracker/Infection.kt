package com.coronatrace.covidtracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infections")
data class Infection(
    @PrimaryKey(autoGenerate = true) val localId: Int,
    val id: String?,
    val infectedTimestamp: Long?, // Only for locally reported/post infection (cannot get this)
    val detectionSource: String // TODO - consider using an enum
)