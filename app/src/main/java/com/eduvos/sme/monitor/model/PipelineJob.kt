package com.eduvos.sme.monitor.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pipeline_jobs")
data class PipelineJob(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientId: Int = 0,
    val jobTitle: String,
    val status: String,
    val amount: Double
)