package com.batman.ecms.features.main.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(title: String) {
    var showMenu by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    if (isSearching) {
        // Expanded Search Mode
        TopAppBar(
            title = {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                )
            },
            navigationIcon = {
                IconButton(onClick = { isSearching = false }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { query = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        // Show keyboard automatically when entering search
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            // Use LocalFocusManager + FocusRequester for auto focus
        }
    } else {
        // Normal App Bar
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = if (title == "ECMS") FontWeight.Bold else FontWeight.Normal,
                    color = if (title == "ECMS")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onBackground
                )
            },
            actions = {
                if (title == "ECMS") {
                    IconButton(onClick = { isSearching = true }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "About us",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(Icons.Outlined.Info, contentDescription = "Info")
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.shadow(4.dp)
        )
    }
}
