import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campuskonnekt.ui.screens.main.sections.*

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Feed : BottomNavItem("feed", "Feed", Icons.Default.Home)
    object Events : BottomNavItem("events", "Events", Icons.Default.Event)
    object StudyGroups : BottomNavItem("study_groups", "Study", Icons.Default.School)
    object Services : BottomNavItem("services", "Services", Icons.Default.Work)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Feed,
        BottomNavItem.Events,
        BottomNavItem.StudyGroups,
        BottomNavItem.Services,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Feed.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Feed.route) { FeedScreen() }
            composable(BottomNavItem.Events.route) { EventsScreen() }
            composable(BottomNavItem.StudyGroups.route) { StudyGroupsScreen() }
            composable(BottomNavItem.Services.route) { ServicesScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }
        }
    }
}