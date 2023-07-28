package com.andikas.data.dao.auth

import com.andikas.models.request.auth.AuthBody
import com.andikas.models.response.auth.UserResponse

interface UserDao {
    suspend fun allUsers(): List<UserResponse>
    suspend fun user(id: Long): UserResponse?
    suspend fun userByUsername(username: String): UserResponse?
    suspend fun addUser(body: AuthBody, salt: String): UserResponse
    suspend fun editUser(id: Long, body: AuthBody, salt: String): Boolean
    suspend fun deleteUser(id: Long): Boolean
}