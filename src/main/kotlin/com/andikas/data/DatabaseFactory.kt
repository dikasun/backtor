package com.andikas.data

import com.andikas.models.entity.auth.Users
import com.andikas.models.entity.personal.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

fun hikari(url: String, user: String, pass: String) = HikariDataSource(
    HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = url
        username = user
        password = pass
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
)

object DatabaseFactory {
    fun init(dbUrl: String, dbUsername: String, dbPass: String) {
        val database = Database.connect(hikari(dbUrl, dbUsername, dbPass))

        transaction(database) {
            SchemaUtils.apply {
                create(Users, Profiles, Educations, Experiences, Skills, Projects, Certifications)
                createMissingTablesAndColumns(Users, Profiles, Educations, Experiences, Skills, Projects, Certifications)
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}