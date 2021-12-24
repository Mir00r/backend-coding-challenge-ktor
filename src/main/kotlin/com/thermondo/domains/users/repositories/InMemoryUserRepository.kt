package com.thermondo.domains.users.repositories

import com.thermondo.domains.users.models.User

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
class InMemoryUserRepository : IUserRepository {

    private val credentialsToUsers = mapOf<String, User>(
        "admin:admin" to User(1, "admin", "Administration", "admin"),
        "abdur:1234" to User(2, "abdur", "Abdur Razzak", "1234")
    )

    override fun getUser(username: String, password: String): User? {
        return credentialsToUsers["$username:$password"]
    }
}
