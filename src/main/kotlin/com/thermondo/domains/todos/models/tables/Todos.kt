package com.thermondo.domains.todos.models.tables

import com.thermondo.domains.users.models.tables.Users
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
object Todos: Table() {
    val id : Column<Int> = integer("id").autoIncrement().primaryKey()
    val userId : Column<Int> = integer("userId").references(Users.userId)
    val title = varchar("title", 256)
    val body = varchar("body", 512)
    val tags = varchar("tags", 256)
}
