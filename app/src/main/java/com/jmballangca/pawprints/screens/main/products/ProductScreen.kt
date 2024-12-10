package com.jmballangca.pawprints.screens.main.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.products.ProductType
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.screens.main.home.HomeEvents
import com.jmballangca.pawprints.screens.main.home.HomeState


@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    state : HomeState,
    events :(HomeEvents) -> Unit,
    navHostController: NavHostController
) {
    var selectedTab by remember {
        mutableStateOf(ProductType.SERVICES)
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        userScrollEnabled = false
    ) {
        item {
            Row {
                TabItem(
                    title = "Services",
                    isSelected = selectedTab == ProductType.SERVICES,
                    onSelect = {selectedTab = ProductType.SERVICES}
                )
                TabItem(
                    title = "Products",
                    isSelected = selectedTab == ProductType.GOODS,
                    onSelect = {selectedTab = ProductType.GOODS}
                )
            }
        }
        val filteredProducts = state.products.filter { it.type == selectedTab }
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

@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize().padding(8.dp).clickable {
            onSelect()
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            title, style = MaterialTheme.typography.titleMedium.copy(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
        )
        Spacer(
            modifier = modifier.height(4.dp)
        )
        Box(
            modifier = modifier.width(24.dp).height(4.dp).background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(2.dp)
            )
        )

    }

}
