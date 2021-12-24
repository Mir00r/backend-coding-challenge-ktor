package com.thermondo.domains.todos.repositories

import com.thermondo.domains.todos.models.data.Todo


/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
interface ITodoRepository {
    suspend fun add(userId: Int, title: String, body: String, tags: String): Todo?
    suspend fun delete(userId: Int, todoId: Int)
    suspend fun find(userId: Int, todoId: Int): Todo?
    suspend fun get(userId: Int): List<Todo>
    suspend fun get(userId: Int, title: String?, tags: String?, offset: Int, limit: Int = 100): List<Todo>
}
