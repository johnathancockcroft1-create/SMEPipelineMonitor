package com.eduvos.sme.monitor.data
import androidx.room.*
import com.eduvos.sme.monitor.model.SMEClient

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: SMEClient)

    @Query("SELECT * FROM sme_clients")
    suspend fun getAll(): List<SMEClient>

    @Delete
    suspend fun delete(client: SMEClient)

    @Update
    suspend fun update(client: SMEClient)
}