package com.eduvos.sme.monitor.data
import androidx.room.*
import com.eduvos.sme.monitor.model.PipelineJob

@Dao
interface PipelineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: PipelineJob)

    @Query("SELECT * FROM pipeline_jobs")
    suspend fun getAll(): List<PipelineJob>

    @Delete
    suspend fun delete(job: PipelineJob)

    @Update
    suspend fun update(job: PipelineJob)
}