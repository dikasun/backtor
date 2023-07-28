package com.andikas.models.entity.auth

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Users : LongIdTable() {
    val username = varchar("username", 1024)
    val password = varchar("password", 1024)
    val salt = varchar("salt", 1024)
}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(Users)

    var username by Users.username
    var password by Users.password
    var salt by Users.salt
}