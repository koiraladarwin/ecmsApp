package com.batman.ecms

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

object AuthUserObject {
    suspend fun getJwt(): String {
        val user = FirebaseAuth.getInstance().currentUser ?: return "null"
        val token = user.getIdToken(false).await().token
        return token?.let { "Bearer $it" } ?: "null"
    }
    suspend fun getUser(): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser
        return user
    }
}
