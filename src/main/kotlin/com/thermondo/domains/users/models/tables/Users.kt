package com.thermondo.domains.users.models.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
object Users : Table() {
    val userId : Column<Int> = integer("id").autoIncrement().primaryKey()
    val userName = varchar("username", 128).uniqueIndex()
    val displayName = varchar("display_name", 256)
    val password = varchar("password", 64)
}
