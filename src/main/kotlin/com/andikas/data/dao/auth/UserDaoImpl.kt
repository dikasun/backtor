package com.andikas.data.dao.auth

import com.andikas.data.DatabaseFactory.dbQuery
import com.andikas.models.entity.auth.UserEntity
import com.andikas.models.entity.auth.Users
import com.andikas.models.request.auth.AuthBody
import com.andikas.models.response.auth.UserResponse

class UserDaoImpl : UserDao {
    override suspend fun allUsers(): List<UserResponse> = dbQuery {
        UserEntity.all().map { entity -> mapToResponse(entity) }
    }

    override suspend fun user(id: Long): UserResponse? = dbQuery {
        UserEntity.findById(id)?.let { mapToResponse(it) }
    }

    override suspend fun userByUsername(username: String): UserResponse? = dbQuery {
        UserEntity.find { Users.username eq username }.firstOrNull()?.let {
            mapToResponse(it)
        }
    }

    override suspend fun addUser(body: AuthBody, salt: String): UserResponse = dbQuery {
        UserEntity.new {
            this.username = body.username
            this.password = body.password
            this.salt = salt
        }.let { mapToResponse(it) }
    }

    override suspend fun editUser(id: Long, body: AuthBody, salt: String): Boolean = dbQuery {
        UserEntity.findById(id)?.let {
            it.username = body.username
            it.password = body.password
            it.salt = salt
        } != null
    }

    override suspend fun deleteUser(id: Long): Boolean = dbQuery {
        UserEntity.findById(id)?.delete() != null
    }

    private fun mapToResponse(entity: UserEntity) = UserResponse(
        id = entity.id.value,
        username = entity.username,
        password = entity.password,
        salt = entity.salt
    )
}

val userDao: UserDao = UserDaoImpl()