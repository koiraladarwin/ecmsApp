package com.batman.ecms.features.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import com.batman.ecms.R

class GoogleAuthUiClient(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)

    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(false)
        .build()

    suspend fun beginSignIn(): IntentSender? {
        val result = oneTapClient.beginSignIn(signInRequest).await()
        return result.pendingIntent.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): UserData? {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val idToken = credential.googleIdToken ?: return null

        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(firebaseCredential).await()
        val user = authResult.user ?: return null
        val jwt = user.getIdToken(true).await().token

        return UserData(
            userId = user.uid,
            userName = user.displayName ?: "batman",
            profilePictureUrl = user.photoUrl?.toString() ?: "",
            jwtToken = jwt ?: ""
        )
    }

    fun signOut() {
        auth.signOut()
    }


    suspend fun getSignedInUser(): UserData? {
        val user = auth.currentUser ?: return null
        val jwt = user.getIdToken(false).await().token

        return UserData(
            userId = user.uid,
            userName = user.displayName ?: "batman",
            profilePictureUrl = user.photoUrl?.toString() ?: "",
            jwtToken = jwt ?: ""
        )
    }

}
