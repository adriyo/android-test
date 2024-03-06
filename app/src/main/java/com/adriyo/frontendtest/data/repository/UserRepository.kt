package com.adriyo.frontendtest.data.repository

import com.adriyo.frontendtest.R
import com.adriyo.frontendtest.data.db.dao.UserDao
import com.adriyo.frontendtest.data.db.mapper.EntityMapper
import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.shared.ItemOption
import com.adriyo.frontendtest.shared.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val entityMapper: EntityMapper,
    private val resource: Resource,
) {

    fun getUsers(): Flow<List<User>> {
        return userDao.getUsers().map { users ->
            users?.map { entity -> entityMapper.getUser(entity) } ?: emptyList()
        }
    }

    suspend fun updateUser(
        selectedUser: User,
        loggedInUser: User,
        inputEmail: String,
        inputUsername: String,
        inputNewPassword: String = "",
        inputRole: ItemOption<Int>,
    ): Result<String> {
        return try {
            if (inputEmail != selectedUser.email) {
                val checkExistingRegisteredEmail = userDao.getRegisteredEmail(inputEmail)
                if (checkExistingRegisteredEmail != null) {
                    return Result.failure(Exception(resource.getString(R.string.err_email_registered)))
                }
            }
            if (inputUsername != selectedUser.username) {
                val checkExistingRegisteredUsername = userDao.getRegisteredUsername(inputUsername)
                if (checkExistingRegisteredUsername != null) {
                    return Result.failure(Exception(resource.getString(R.string.err_username_registered)))
                }
            }
            var updatedEntity = entityMapper.getUserEntity(selectedUser).copy(
                email = inputEmail,
                username = inputUsername,
                role = inputRole.value,
            )
            if (loggedInUser.id == selectedUser.id) {
                if (inputNewPassword.isNotEmpty()) {
                    updatedEntity = updatedEntity.copy(
                        password = inputNewPassword,
                        loggedInStatus = true
                    )
                }
            }
            userDao.update(updatedEntity)
            Result.success(resource.getString(R.string.msg_edit_user_success))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(selectedUser: User): Result<String> {
        return try {
            userDao.delete(entityMapper.getUserEntity(selectedUser))
            Result.success(resource.getString(R.string.msg_delete_user_success))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
