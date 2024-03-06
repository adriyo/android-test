package com.adriyo.frontendtest.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adriyo.frontendtest.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@Dao
abstract class UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun save(user: UserEntity)

    @Query("SELECT * FROM users WHERE email=:email AND password=:password")
    abstract suspend fun getInfo(email: String, password: String): UserEntity?

    @Query("UPDATE users SET loggedInStatus=1 WHERE email=:email")
    abstract suspend fun updateLoggedInStatus(email: String)

    @Query("UPDATE users SET loggedInStatus=0")
    abstract suspend fun resetLoggedInStatus()

    @Query("SELECT * FROM users WHERE loggedInStatus=1 LIMIT 1")
    abstract suspend fun getLoggedInUser(): UserEntity?

    @Query("SELECT * FROM users ORDER BY id ASC")
    abstract fun getUsers(): Flow<List<UserEntity>?>

    @Update
    abstract suspend fun update(userEntity: UserEntity)

    @Delete
    abstract suspend fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE email=:email")
    abstract suspend fun getRegisteredEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE username=:username")
    abstract suspend fun getRegisteredUsername(username: String): UserEntity?
}