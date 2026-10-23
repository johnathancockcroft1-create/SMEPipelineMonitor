package com.eduvos.sme.monitor.data
import com.eduvos.sme.monitor.model.PipelineJob
import com.eduvos.sme.monitor.model.SMEClient
import com.eduvos.sme.monitor.model.User

class Repository(private val db: AppDatabase) {
    suspend fun getUser(username: String, password: String) = db.userDao().getUser(username, password)
    suspend fun registerUser(user: User) = db.userDao().insert(user)
    suspend fun getAllUsers() = db.userDao().getAll()

    suspend fun insertClient(client: SMEClient) = db.clientDao().insert(client)
    suspend fun getAllClients() = db.clientDao().getAll()
    suspend fun deleteClient(client: SMEClient) = db.clientDao().delete(client)
    suspend fun updateClient(client: SMEClient) = db.clientDao().update(client)

    suspend fun insertJob(job: PipelineJob) = db.pipelineDao().insert(job)
    suspend fun getAllJobs() = db.pipelineDao().getAll()
    suspend fun deleteJob(job: PipelineJob) = db.pipelineDao().delete(job)
    suspend fun updateJob(job: PipelineJob) = db.pipelineDao().update(job)
}