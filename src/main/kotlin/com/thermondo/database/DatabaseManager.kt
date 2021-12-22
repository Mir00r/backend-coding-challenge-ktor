package com.thermondo.database

import com.thermondo.domains.todos.models.tables.Todos
import com.thermondo.domains.users.models.tables.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.ktorm.database.Database

/**
 * @project IntelliJ IDEA
 * @author mir00r on 22/12/21
 */
class DatabaseManager {
    // config
    private val hostname = "vm-core.fritz.box"
    private val databaseName = "ktor_todo"
    private val username = "root"
    private val password = System.getenv("KTOR_TODOLIST_DB_PW")

    // database
    private val ktormDatabase: Database

    init {
        val jdbcUrl =
            "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false&serverTimezone=Europe/Berlin"
        ktormDatabase = Database.connect(jdbcUrl)
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Todos)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}
