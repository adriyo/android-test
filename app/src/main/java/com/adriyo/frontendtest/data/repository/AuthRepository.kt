package com.adriyo.frontendtest.data.repository

import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.data.db.dao.UserDao
import com.adriyo.frontendtest.data.db.entity.UserEntity
import com.adriyo.frontendtest.data.db.mapper.EntityMapper
import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.shared.Resource
import javax.inject.Inject

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val resource: Resource,
    private val entityMapper: EntityMapper,
) {

    suspend fun register(
        username: String,
        email: String,
        role: Int,
        password: String,
    ): Result<String> {
        return try {
            val checkExistingRegisteredEmail = userDao.getRegisteredEmail(email)
            if (checkExistingRegisteredEmail != null) {
                return Result.failure(Exception(resource.getString(R.string.err_email_registered)))
            }

            val checkExistingRegisteredUsername = userDao.getRegisteredUsername(username)
            if (checkExistingRegisteredUsername != null) {
                return Result.failure(Exception(resource.getString(R.string.err_username_registered)))
            }
            userDao.save(
                UserEntity(
                    username = username,
                    email = email,
                    role = role,
                    password = password
                )
            )
            Result.success(resource.getString(R.string.msg_register_success))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        val result = userDao.getInfo(email, password)
        if (result == null) {
            return Result.failure(Exception(resource.getString(R.string.err_account_not_found)))
        }
        return try {
            userDao.updateLoggedInStatus(email = email)
            Result.success(entityMapper.getUser(result))
        } catch (e: Exception) {
            Result.failure(Exception(resource.getString(R.string.err_login_failed)))
        }
    }

    suspend fun logout(): Result<String> {
        return try {
            userDao.resetLoggedInStatus()
            Result.success(resource.getString(R.string.msg_logout_success))
        } catch (e: Exception) {
            Result.failure(Exception(resource.getString(R.string.err_account_not_found)))
        }
    }

    suspend fun getLoggedInUser(): User? {
        val user = userDao.getLoggedInUser() ?: return null
        return entityMapper.getUser(user)
    }

}