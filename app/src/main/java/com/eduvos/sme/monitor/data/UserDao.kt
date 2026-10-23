package com.eduvos.sme.monitor.data
import androidx.room.*
import com.eduvos.sme.monitor.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUser(username: String, password: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>
}