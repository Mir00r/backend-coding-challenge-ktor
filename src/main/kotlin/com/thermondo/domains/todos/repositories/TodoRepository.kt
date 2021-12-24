package com.thermondo.domains.todos.repositories

import com.thermondo.database.DatabaseManager.dbQuery
import com.thermondo.domains.todos.models.data.Todo
import com.thermondo.domains.todos.models.tables.Todos
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
class TodoRepository : ITodoRepository {
    override suspend fun add(userId: Int, title: String, body: String, tags: String): Todo? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = Todos.insert {
                it[Todos.userId] = userId
                it[Todos.title] = title
                it[Todos.body] = body
                it[Todos.tags] = tags
            }
        }
        return rowToTodo(statement?.resultedValues?.get(0))
    }

    override suspend fun delete(userId: Int, todoId: Int) {
        dbQuery {
            Todos.deleteWhere {
                Todos.id.eq(todoId) and
                        Todos.userId.eq(userId)
            }
        }
    }

    override suspend fun find(userId: Int, todoId: Int): Todo? {
        return dbQuery {
            Todos.select {
                Todos.id.eq(todoId) and
                        Todos.userId.eq((userId))
            }.map { rowToTodo(it) }.singleOrNull()
        }
    }

    override suspend fun get(userId: Int): List<Todo> {
        return dbQuery {
            Todos.select {
                Todos.userId.eq((userId))
            }.mapNotNull { rowToTodo(it) }
        }
    }

    override suspend fun get(userId: Int, title: String?, tags: String?, offset: Int, limit: Int): List<Todo> {
        return dbQuery {
            Todos.select {
                if (title != null && tags != null) Todos.title.eq(title).and(Todos.tags.eq(tags)).and(Todos.userId.eq((userId)))
                else if (title != null && tags == null) Todos.title.eq(title).and(Todos.userId.eq((userId)))
                else if (title == null && tags != null) Todos.tags.eq(tags).and(Todos.userId.eq((userId)))
                else
                    Todos.userId.eq((userId))
            }.limit(limit, offset = offset).mapNotNull { rowToTodo(it) }
        }
    }

    private fun rowToTodo(row: ResultRow?): Todo? {
        if (row == null) {
            return null
        }
        return Todo(
            id = row[Todos.id],
            userId = row[Todos.userId],
            title = row[Todos.title],
            body = row[Todos.body],
            tags = row[Todos.tags]
        )
    }
}
