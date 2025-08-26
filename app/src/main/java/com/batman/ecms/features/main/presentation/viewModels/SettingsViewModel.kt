package com.batman.ecms.features.main.presentation.viewModels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.batman.ecms.AuthUserObject
import kotlinx.coroutines.launch

data class SettingItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

data class PersonInfo(
    val name: String = "",
    val email: String = "",
    val img: String = "",
)

class SettingsViewModel : ViewModel() {
    val personInfo = mutableStateOf<PersonInfo>(PersonInfo())

    init {
        viewModelScope.launch {
            val user = AuthUserObject.getUser();
            user?.let{user->
                personInfo.value = PersonInfo(
                    name = user.displayName.toString(),
                    email = user.email.toString(),
                    img =user.photoUrl.toString()
                )
            }
        }
    }

    val settings = listOf(
        SettingItem(
            Icons.Default.Person,
            "Account",
            "Security notifications, change number"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Lock,
            "Privacy",
            "Block contacts, disappearing messages"
        ) { /* TODO */ },
        SettingItem(Icons.Default.Face, "Avatar", "Create, edit, profile photo") { /* TODO */ },
        SettingItem(
            Icons.AutoMirrored.Default.Chat,
            "Chats",
            "Theme, wallpapers, chat history"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Notifications,
            "Notifications",
            "Message, group & call tones"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Storage,
            "Storage and data",
            "Network usage, auto-download"
        ) { /* TODO */ },
        SettingItem(
            Icons.Default.Language,
            "App language",
            "English (device's language)"
        ) { /* TODO */ },
        SettingItem(
            Icons.AutoMirrored.Default.Help,
            "Help",
            "Help centre, contact us, privacy policy"
        ) { /* TODO */ }
    )


}