package com.jmballangca.pawprints.screens.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.models.appointment.AppointmentStatus
import com.jmballangca.pawprints.models.products.ProductType
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.screens.main.products.ProductCard
import com.jmballangca.pawprints.screens.main.products.ProductScreen
import com.jmballangca.pawprints.screens.main.products.TabItem
import com.jmballangca.pawprints.ui.custom.PawPrintFeaturesButton
import com.jmballangca.pawprints.utils.toast


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state : HomeState,
    events: (HomeEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (state.users != null) {
            state.users.id?.let {
                events(HomeEvents.OnGetAppointments(it))
            }
        }
    }
    LaunchedEffect(state) {
        if (state.cartAdded != null) {
            context.toast(state.cartAdded)
        }
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }
        LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.banner),
                    contentDescription = "banner",
                    contentScale = ContentScale.Crop,
                    modifier = modifier.fillMaxWidth().height(200.dp).padding(16.dp).clip(
                        shape = MaterialTheme.shapes.medium
                    )
                )
            }
            item {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    PawPrintFeaturesButton(
                        label = "Appointments",
                        icon = Icons.Filled.CalendarMonth,
                        onClick = { navHostController.navigate(AppRouter.Appointments.route )}
                    )
                    PawPrintFeaturesButton(
                        label = "Transactions",
                        icon = Icons.Filled.StackedBarChart,
                        onClick = { navHostController.navigate(AppRouter.Transactions.route )}
                    )
                    PawPrintFeaturesButton(
                        label = "Inbox",
                        icon = Icons.Filled.Inbox,
                        onClick = { navHostController.navigate(AppRouter.Inbox.route )}
                    )
                }
            }


            val filtered = state.appointments.filter {
                it.status == AppointmentStatus.CONFIRMED
            }
            if (filtered.isNotEmpty()) {
                item {
                    Card(
                        modifier = modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Row(
                            modifier =modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Announcement,
                                contentDescription = "Announcements"
                            )
                            Text(
                                "You have ${filtered.size} upcoming appointments.",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            item {
                Row {
                    TabItem(
                        title = "Services",
                        isSelected = state.selectedTab == ProductType.SERVICES,
                        onSelect = {events(HomeEvents.OnTabSelected(ProductType.SERVICES))}
                    )
                    TabItem(
                        title = "Products",
                        isSelected = state.selectedTab == ProductType.GOODS,
                        onSelect = {events(HomeEvents.OnTabSelected(ProductType.GOODS))}
                    )
                }
            }
            val filteredProducts = state.products.filter { it.type == state.selectedTab }
            items(filteredProducts) {
                ProductCard(product = it,
                    onClick = {
                        it.id?.let { id->
                            navHostController.navigate(AppRouter.ViewProductRoute.navigate(id))
                        }
                    }, onAddToCart = {
                        events(HomeEvents.AddToCart(it))
                    }, isAdding = it.id == state.adding)
            }
        }
}