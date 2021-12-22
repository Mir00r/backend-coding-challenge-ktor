package com.thermondo.commons

/**
 * @project IntelliJ IDEA
 * @author mir00r on 23/12/21
 */
class Constants {

    companion object {
        const val API_VERSION = "/v1"

        // jwt config
        const val JWT_ISSUER = "com.thermondo"
        const val JWT_REALM = "com.thermondo.todos"

        // claims
        const val CLAIM_USERID = "userId"
        const val CLAIM_USERNAME = "userName"
    }
}
