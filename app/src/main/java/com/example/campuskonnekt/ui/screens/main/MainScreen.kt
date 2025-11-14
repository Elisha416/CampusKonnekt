package com.example.campuskonnekt.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campuskonnekt.ui.screens.main.sections.*
import kotlinx.coroutines.launch

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomNavItems = listOf(
        BottomNavItem.Feed,
        BottomNavItem.Events,
        BottomNavItem.StudyGroups,
        BottomNavItem.Services,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                    scope.launch { drawerState.close() }
                },
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (currentRoute in listOf("chapel", "cueaso", "sports")) {
                    // CUEA sections have their own top bars
                } else {
                    TopAppBar(
                        title = {
                            Column {
                                Text("Campus Connect")
                                Text(
                                    "CUEA",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            },
            bottomBar = {
                if (currentRoute !in listOf("chapel", "cueaso", "sports")) {
                    NavigationBar {
                        val currentDestination = navBackStackEntry?.destination

                        bottomNavItems.forEach { item ->
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
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Feed.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                // Bottom Nav Screens
                composable(BottomNavItem.Feed.route) { FeedScreen() }
                composable(BottomNavItem.Events.route) { EventsScreen() }
                composable(BottomNavItem.StudyGroups.route) { StudyGroupsScreen() }
                composable(BottomNavItem.Services.route) { ServicesScreen() }
                composable(BottomNavItem.Profile.route) { ProfileScreen() }

                // CUEA-Specific Screens (from drawer)
                composable("chapel") { ChapelScreen() }
                composable("cueaso") { CUEASOScreen() }
                composable("sports") { CUEASportsScreen() }
            }
        }
    }
}

@Composable
fun DrawerContent(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "CUEA Campus Connect",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Catholic University of Eastern Africa",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Navigation Section
            Text(
                text = "MAIN",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            DrawerItem(
                icon = Icons.Default.Home,
                label = "Feed",
                selected = currentRoute == "feed",
                onClick = {
                    onNavigate("feed")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.Event,
                label = "Events",
                selected = currentRoute == "events",
                onClick = {
                    onNavigate("events")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.School,
                label = "Study Groups",
                selected = currentRoute == "study_groups",
                onClick = {
                    onNavigate("study_groups")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.Work,
                label = "Campus Services",
                selected = currentRoute == "services",
                onClick = {
                    onNavigate("services")
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            // CUEA-Specific Section
            Text(
                text = "CUEA FEATURES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            DrawerItem(
                icon = Icons.Default.Church,
                label = "Chapel & Spiritual Life",
                selected = currentRoute == "chapel",
                onClick = {
                    onNavigate("chapel")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.HowToVote,
                label = "CUEASO",
                description = "Student Government",
                selected = currentRoute == "cueaso",
                onClick = {
                    onNavigate("cueaso")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.SportsRugby,
                label = "Sports & Catholic Monks RFC",
                selected = currentRoute == "sports",
                onClick = {
                    onNavigate("sports")
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            DrawerItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = currentRoute == "profile",
                onClick = {
                    onNavigate("profile")
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                selected = false,
                onClick = {
                    // TODO: Navigate to settings
                    onCloseDrawer()
                }
            )
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    label: String,
    description: String? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = null) },
        label = {
            Column {
                Text(label)
                description?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}
