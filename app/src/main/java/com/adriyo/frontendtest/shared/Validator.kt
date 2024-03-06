package com.adriyo.frontendtest.shared

import com.adriyo.frontendtest.R
import javax.inject.Inject

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
class Validator @Inject constructor(
    private val resource: Resource,
) {

    fun isEmailValid(input: String): Pair<Boolean, String?> {
        if (input.isEmpty()) {
            return Pair(false, resource.getString(R.string.err_empty_email))
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            return Pair(false, resource.getString(R.string.err_invalid_email))
        }
        return Pair(true, null)
    }

    fun isPasswordValid(input: String): Pair<Boolean, String?> {
        if (input.isEmpty()) {
            return Pair(false, resource.getString(R.string.err_empty_password))
        }
        return Pair(true, null)
    }

    fun isRegisterPasswordValid(input: String): Pair<Boolean, String?> {
        if (input.isEmpty() || input.length < 5) {
            return Pair(false, resource.getString(R.string.err_min_length_password))
        }
        return Pair(true, null)
    }

    fun isLoggedInUserPasswordValid(input: String, currentPassword: String?): Pair<Boolean, String?> {
        if (input.isEmpty()) {
            return Pair(false, resource.getString(R.string.err_empty_password))
        }
        if (input != currentPassword) {
            return Pair(false, resource.getString(R.string.err_invalid_password))
        }
        return Pair(true, null)
    }

    fun isUsernameValid(input: String): Pair<Boolean, String?> {
        val regex = Regex("[^a-zA-Z]")
        if (input.isEmpty() || input.length < 5) {
            return Pair(false, resource.getString(R.string.err_min_length_username))
        }
        if (regex.find(input) != null) {
            return Pair(false, resource.getString(R.string.err_invalid_username))
        }
        return Pair(true, null)
    }

    fun isRoleValid(input: ItemOption<Int>?): Pair<Boolean, String?> {
        if (input == null || input.value == -1) {
            return Pair(false, resource.getString(R.string.err_empty_role))
        }
        return Pair(true, null)
    }

}