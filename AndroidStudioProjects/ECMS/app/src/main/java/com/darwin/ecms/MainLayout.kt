package com.darwin.ecms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.darwin.ecms.features.auth.UserData


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
                TopAppBar(title = { Text("Ecms") })
            }
        },
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
fun HomeScreen(navController: NavController) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Gray)
                        .clickable {
                            navController.navigate("inside")
                        }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Gray)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE0E0E0))
            )
        }
    }
}

@Composable
fun SearchScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Search Screen")
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
fun InsideRoute(){
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Inside Screen")
    }
}