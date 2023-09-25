package com.example.newcalendarlibrary.room.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: List<User>)

    @Update
    suspend fun update(user: User)

    @Query("SELECT id FROM users WHERE loginValue = :loginValue")
    suspend fun getUserId(loginValue: String): Int

    @Query("SELECT nameValue FROM users WHERE id = :id")
    suspend fun getUserName(id: Int): String

    @Query("SELECT * FROM users WHERE users.loginValue=:loginValue")
    suspend fun getUserByLogin(loginValue: String): User?

    @Query("SELECT * FROM users WHERE users.loginValue = :loginValue AND users.passwordValue = :passwordValue")
    suspend fun getUserByLoginAndPassword(loginValue: String, passwordValue: String): User?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}