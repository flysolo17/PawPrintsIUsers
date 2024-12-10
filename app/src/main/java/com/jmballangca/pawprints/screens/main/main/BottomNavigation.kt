package com.jmballangca.pawprints.screens.main.main

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomNavBar(
    navController: NavController,
    items: List<NavigationItems>,
    navBackStackEntry : NavBackStackEntry ?
) {
    val currentRoute = navBackStackEntry?.destination
    val bottomBarDestination = items.any { it.route == currentRoute?.route }
    BottomAppBar(containerColor = Color.Transparent) {
        items.forEachIndexed { index, destinations ->
            val isSelected = (currentRoute?.hierarchy?.any {
                it.route == destinations.route
            } == true)
            if(index == 2) {
                FloatingActionButton (
                    shape = CircleShape,
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        navController.navigate(destinations.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Pets,
                        contentDescription = "Pets"
                    )
                }
            } else {
                NavigationBarItem(
                    label = { Text(text = destinations.label)},
                    selected = isSelected,
                    onClick = {
                        navController.navigate(destinations.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, icon = {
                        BadgedBox(badge = {
                            if (destinations.badgeCount != null) {
                                Badge {
                                    Text(text = destinations.badgeCount.toString())
                                }
                            } else if (destinations.hasNews) {
                                Badge()
                            }
                        }) {
                            if (isSelected) {
                                Icon(imageVector = destinations.selectedIcon, contentDescription = destinations.route)
                            } else {
                                Icon(imageVector = destinations.unselectedIcon, contentDescription = destinations.route)
                            }
                        }
                    })
            }
        }
    }
}