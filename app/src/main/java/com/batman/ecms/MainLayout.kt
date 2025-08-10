package com.batman.ecms

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.batman.ecms.features.auth.UserData
import com.batman.ecms.features.main.data.service.RetrofitInstance
import com.batman.ecms.features.main.presentation.components.AddEventDialog
import com.batman.ecms.features.main.presentation.screens.EventInfoScreen
import com.batman.ecms.features.main.presentation.screens.HomeScreen
import com.batman.ecms.features.main.presentation.components.MainTopAppBar
import com.batman.ecms.features.main.presentation.screens.ActivityCheckInScreen
import com.batman.ecms.features.main.presentation.screens.AttendeeCheckInScreen
import com.batman.ecms.features.main.presentation.screens.StaffScreen
import com.batman.ecms.features.main.presentation.screens.AttendeesScreen
import com.batman.ecms.features.main.presentation.screens.ScanAttendeeScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Staff : Screen("staff")
    object Notifications : Screen("alert")
    object Settings : Screen("settings")
    object Profile : Screen("profile")

    object EventInfo : Screen("event_info/{eventId}") {
        fun withId(id: String) = "event_info/$id"
    }

    object Attendees : Screen("attendees/{eventId}") {
        fun withId(id: String) = "attendees/$id"
    }

    object ScanAttendee : Screen("scan_attendee/{activityId}") {
        fun withId(id: String) = "scan_attendee/$id"
    }

    object ActivityCheckIn : Screen("activity_check_in/{activityId}") {
        fun withId(id: String) = "activity_check_in/$id"
    }

    object AttendeeCheckIn : Screen("attendee_check_in/{attendeeId}") {
        fun withId(id: String) = "attendee_check_in/$id"
    }
}

val bottomBarRoutes = listOf(
    Screen.Home.route,
    Screen.Staff.route,
    Screen.Notifications.route,
    Screen.Settings.route,
    Screen.Profile.route
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(user: UserData, onSignOut: () -> Unit) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentRoute in bottomBarRoutes




    Scaffold(

        topBar = {
            when {
                currentRoute == Screen.Home.route -> MainTopAppBar(title = "Home")
                currentRoute == Screen.Staff.route -> MainTopAppBar(title = "Staff")
                currentRoute == Screen.Notifications.route -> MainTopAppBar(title = "Notifications")
                currentRoute == Screen.Settings.route -> MainTopAppBar(title = "Settings")
                currentRoute == Screen.Profile.route -> MainTopAppBar(title = "Profile")
                currentRoute?.startsWith(Screen.EventInfo.route) == true -> MainTopAppBar(title = "Event Info")
                currentRoute?.startsWith(Screen.Attendees.route) == true -> MainTopAppBar(title = "Attendees")
                currentRoute?.startsWith(Screen.ActivityCheckIn.route) == true -> MainTopAppBar(
                    title = "Attendees"
                )

                currentRoute?.startsWith(Screen.AttendeeCheckIn.route) == true -> MainTopAppBar(
                    title = "Attendees"
                )

                else -> {}
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
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            navController = navController,
            startDestination = Screen.Home.route,

            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToEventInfo = { eventId ->
                        navController.navigate(Screen.EventInfo.withId(eventId))
                    },
                    navigateToAttendeeInfo = { eventId ->
                        navController.navigate(Screen.Attendees.withId(eventId))
                    },
                    navigateToStaffInfo = {

                    }
                )
            }
            composable(Screen.Staff.route) { StaffScreen() }
            composable(Screen.Notifications.route) { NotificationsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Profile.route) { ProfileScreen(onSignOut) }
            composable(
                route = Screen.EventInfo.route,
                arguments = listOf(navArgument("eventId") { type = NavType.StringType })
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")
                EventInfoScreen(
                    eventId = eventId.toString(),
                    navigateToScan = { activityId ->
                        navController.navigate(Screen.ScanAttendee.withId(activityId))
                    },
                    navigateToActivityCheckIn = { activityId ->
                        navController.navigate(Screen.ActivityCheckIn.withId(activityId))
                    }
                )
            }
            composable(
                route = Screen.Attendees.route,
                arguments = listOf(navArgument("eventId") { type = NavType.StringType })
            ) { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId")
                AttendeesScreen(
                    eventId = eventId.toString(),
                    onClickCheckInLogs = { userId ->
                        navController.navigate(Screen.AttendeeCheckIn.withId(userId))
                    },
                )
            }
            composable(
                route = Screen.ScanAttendee.route,
                arguments = listOf(navArgument("activityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val activityId = backStackEntry.arguments?.getString("activityId")
                ScanAttendeeScreen(activityId = activityId.toString())
            }
            composable(
                route = Screen.ActivityCheckIn.route,
                arguments = listOf(navArgument("activityId") { type = NavType.StringType })
            ) { backStackEntry ->
                val activityId = backStackEntry.arguments?.getString("activityId")
                ActivityCheckInScreen(activityId = activityId.toString())
            }
            composable(
                route = Screen.AttendeeCheckIn.route,
                arguments = listOf(navArgument("attendeeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val attendeeId = backStackEntry.arguments?.getString("attendeeId")
                AttendeeCheckInScreen(activityId = attendeeId.toString())
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentRoute: String?, onItemSelected: (String) -> Unit) {
    val items = listOf(
        Screen.Home,
        Screen.Staff,
        Screen.Notifications,
        Screen.Settings,
        Screen.Profile
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            is Screen.Home -> Icons.Default.Home
                            is Screen.Staff -> Icons.Default.AccountBox
                            is Screen.Notifications -> Icons.Default.Notifications
                            is Screen.Settings -> Icons.Default.Settings
                            is Screen.Profile -> Icons.Default.Person
                            else -> Icons.Default.Clear
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
fun SettingsScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen")
    }
}

