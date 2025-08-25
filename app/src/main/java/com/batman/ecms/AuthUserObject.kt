package com.batman.ecms

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object AuthUserObject {
    suspend fun getJwt(): String {
        val user = FirebaseAuth.getInstance().currentUser ?: return "null"
        val token = user.getIdToken(false).await().token
        return token?.let { "Bearer $it" } ?: "null"
    }
}
