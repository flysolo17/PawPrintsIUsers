package com.jmballangca.pawprints.screens.main.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.router.MainNavGraph
import com.jmballangca.pawprints.utils.ProfileImage
import com.jmballangca.pawprints.utils.ProgressBar
import com.jmballangca.pawprints.utils.UnknownError
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainNav: NavHostController,
    state: MainState,
    events: (MainEvents) -> Unit
) {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navItems = BOTTOM_ITEMS
    val drawerItems = DRAWER_ITEMS
    val isDrawerItems = drawerItems.any { it.route == currentRoute }
    val isNavItems =navItems.any { it.route == currentRoute }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.carts) {
        navItems[1].badgeCount = state.carts.size
    }
    when {

        state.isLoading -> {
            ProgressBar(
                title = "Getting User Info"
            )
        }
        state.isGettingUserCart -> {
            ProgressBar(
                title = "Getting user cart"
            )
        }
        state.errors != null -> {
            UnknownError(
                title = state.errors
            ) {
                Button(
                    onClick = { mainNav.popBackStack() }
                ) { Text("Back") }
            }
        } state.users != null ->  {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                DrawerContent(
                    currentRoute = currentRoute,
                    navHostController = navHostController,
                    items = drawerItems,
                    onClose = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
            ) {
            Scaffold(
                topBar = {
                    if (isNavItems || isDrawerItems) {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            title = {
                                Text("PawPrints")
                            },
                            navigationIcon = {
                                val isNavRoute = navItems.any {
                                    it.route == currentRoute
                                } || drawerItems.any {
                                    it.route == currentRoute
                                }
                                if (isNavRoute) {
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                drawerState.open()
                                            }
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                } else {
                                    IconButton(
                                        onClick = {navHostController.popBackStack()}
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "back"
                                        )
                                    }
                                }
                            },
                            actions = {
                                ProfileImage(
                                    modifier = modifier.size(32.dp),
                                    image = state.users.profile ?: ""
                                )
                            }
                        )
                    }

                },
                bottomBar = {
                    if (isNavItems) {
                        BottomNavBar(
                            navController = navHostController,
                            items = navItems,
                            navBackStackEntry
                        )
                    }
                }
            ) {
                Box(modifier = modifier
                    .fillMaxSize()
                    .padding(it)) {
                    MainNavGraph(navHostController = navHostController, mainNav = mainNav ,users = state.users, carts = state.carts)
                }
            }
        }
        } else -> {
        UnknownError(
            title = "No user found!"
        ) {
            Button(
                onClick = {mainNav.popBackStack()}
            ) { Text("Back") }
        }
        }
    }
}



@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    currentRoute : String?,
    navHostController: NavHostController,
    items : List<NavigationItems>,
    onClose : () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                painter = painterResource(R.drawable.paw_logo),
                contentDescription = "Logo",
                modifier =  modifier.size(100.dp)
            )

            Text(
                "PAWPRINTS",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Column(
            modifier = modifier.padding(8.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = currentRoute == item.route
                NavigationDrawerItem(
                    label = {
                        Text(item.label)
                    },
                    shape = MaterialTheme.shapes.small,
                    selected = isSelected,
                    icon = {
                        if (isSelected) {
                            Icon(imageVector = item.selectedIcon, contentDescription = item.label)
                        } else {
                            Icon(imageVector = item.unselectedIcon, contentDescription = item.label)
                        }

                    },
                    onClick = {
                        navHostController.navigate(item.route) {
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        onClose()
                    }
                )
            }

        }
    }

}
