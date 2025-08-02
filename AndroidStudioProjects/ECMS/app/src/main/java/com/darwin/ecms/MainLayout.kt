package com.darwin.ecms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.darwin.ecms.features.auth.UserData
import com.darwin.ecms.features.main.presentation.HomeScreen


sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Staff : Screen("staff")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}

val bottomBarRoutes = listOf(
    Screen.Home.route,
    Screen.Staff.route,
    Screen.Notifications.route,
    Screen.Settings.route
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(user: UserData, onSignOut: () -> Unit) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentRoute in bottomBarRoutes
    val shouldShowTopBar = currentRoute in bottomBarRoutes

    Scaffold(


        topBar = {
            if (shouldShowTopBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = "ECMS",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },

                    actions = {
                        IconButton(onClick = { /* Handle settings */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Options",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                )
            }
        }
,

        floatingActionButton = {
            if (currentRoute == Screen.Home.route) {
                FloatingActionButton(onClick = { /* TODO */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        },
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(currentRoute = currentRoute) { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
            .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Staff.route) { SearchScreen() }
            composable(Screen.Notifications.route) { NotificationsScreen() }
            composable(Screen.Settings.route) { ProfileScreen(onSignOut) }
            composable("inside") { InsideRoute() }
        }
    }
}

@Composable
fun BottomNavigationBar(currentRoute: String?, onItemSelected: (String) -> Unit) {
    val items = listOf(
        Screen.Home,
        Screen.Staff,
        Screen.Notifications,
        Screen.Settings
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            is Screen.Home -> Icons.Default.Home
                            is Screen.Staff -> Icons.Default.Person
                            is Screen.Notifications -> Icons.Default.Notifications
                            is Screen.Settings -> Icons.Default.Settings
                        },
                        contentDescription = screen.route
                    )
                },
                label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen.route) }
            )
        }
    }
}

@Composable
fun AdmissionCard(
    name: String,
    code: String,
    ageGroup: String,
    admissions: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(10.dp)
            .height(100.dp)  // exact height limit
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), // fill the Box
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Vertical blue bar
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .width(4.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(Color(0xFF1976D2))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Profile image
            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info section — weight fills horizontal space but height wraps content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = code,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Text(
                    text = ageGroup,
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = admissions,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SearchScreen() {
    Column(){
        AdmissionCard(
            name = "Giulio Ciccone",
            code = "MEM-1",
            ageGroup = "Executive",
            admissions = "OCS Business Solution",
            imageUrl = "https://randomuser.me/api/portraits/men/32.jpg",
        )
        AdmissionCard(
            name = "Bruce Wayne",
            code = "MEM-2",
            ageGroup = "Executive",
            admissions = "OCS Business Solution",
            imageUrl = "https://randomuser.me/api/portraits/men/30.jpg",
        )
    }

}


@Composable
fun NotificationsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Notifications Screen")
    }
}

@Composable
fun ProfileScreen(onSignOut: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}

@Composable
fun InsideRoute() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Inside Screen")
    }
}