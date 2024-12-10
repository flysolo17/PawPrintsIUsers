package com.jmballangca.pawprints.screens.main.main

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.screens.main.cart.CartScreen
import com.jmballangca.pawprints.screens.main.home.HomeScreen
import com.jmballangca.pawprints.screens.main.messages.MessageScreen
import com.jmballangca.pawprints.screens.main.profile.ProfileScreen
data class NavigationItems(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    var badgeCount: Int? = null,
    val route: String
) {

}
    val BOTTOM_ITEMS    : List<NavigationItems> =
         listOf(
            NavigationItems(
                label = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                hasNews = false,
                route = AppRouter.HomeScreen.route
            ),
            NavigationItems(
                label = "Cart",
                selectedIcon = Icons.Filled.ShoppingCart,
                unselectedIcon = Icons.Outlined.ShoppingCart,
                hasNews = false,
                route = AppRouter.CartScreen.route
            ),
             NavigationItems(
                 label = "Pets",
                 selectedIcon = Icons.Filled.Pets,
                 unselectedIcon = Icons.Outlined.Pets,
                 hasNews = false,
                 route = AppRouter.PetScreen.route
             ),
            NavigationItems(
                label = "Messages",
                selectedIcon = Icons.Filled.Message,
                unselectedIcon = Icons.Outlined.Message,
                hasNews = true,
                route = AppRouter.MessageScreen.route
            ),
            NavigationItems(
                label = "Profile",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            )
        )




    val DRAWER_ITEMS :  List<NavigationItems> =
        listOf(
            NavigationItems(
                label = "Profile",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            ),
            NavigationItems(
                label = "About",
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Outlined.Info,
                hasNews = false,
                route = AppRouter.AboutScreen.route
            ),
            NavigationItems(
                label = "Developers",
                selectedIcon = Icons.Filled.Code,
                unselectedIcon = Icons.Outlined.Code,
                hasNews = false,
                route = AppRouter.DevelopersScreen.route
            ),
            NavigationItems(
                label = "Credits",
                selectedIcon = Icons.Filled.Star,
                unselectedIcon = Icons.Outlined.Star,
                hasNews = false,
                route = AppRouter.CreditsScreen.route
            ),
            NavigationItems(
                label = "Clinic",
                selectedIcon = Icons.Filled.LocalHospital,
                unselectedIcon = Icons.Outlined.LocalHospital,
                hasNews = false,
                route = AppRouter.ClinicScreen.route
            )
        )

