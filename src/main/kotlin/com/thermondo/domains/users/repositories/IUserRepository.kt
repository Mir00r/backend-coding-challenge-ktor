package com.thermondo.domains.users.repositories

import com.thermondo.domains.users.models.User

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
interface IUserRepository {
    fun getUser(username: String, password: String): User?

}
