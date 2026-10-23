package com.eduvos.sme.monitor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sme_clients")
data class SMEClient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val businessType: String
)