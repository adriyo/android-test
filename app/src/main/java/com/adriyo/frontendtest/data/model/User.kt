package com.adriyo.frontendtest.data.model

data class User(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    val role: Int = -1,
    val password: String = ""
) {
    fun getRoleName(): String {
        if (role == UserRole.REGULAR) {
            return "Regular"
        }
        return "Admin"
    }
}
