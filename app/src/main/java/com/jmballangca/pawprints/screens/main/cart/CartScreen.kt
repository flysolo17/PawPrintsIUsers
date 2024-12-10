package com.jmballangca.pawprints.screens.main.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.cart.CartWithProduct
import com.jmballangca.pawprints.utils.Description
import com.jmballangca.pawprints.utils.RoundedImage
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.getAllQuantity
import com.jmballangca.pawprints.utils.toPhp
import com.jmballangca.pawprints.utils.toast


@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    state: CartState,
    events: (CartEvents) -> Unit,
    navHostController: NavHostController,
) {
    val context = LocalContext.current
    LaunchedEffect(state) {
        if (state.errors != null) {
            context.toast(state.errors)
        }
        if (state.updated != null) {
            context.toast(state.updated)
        }
    }
    Column(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            items(state.carts) {
                CartItem(
                    cartWithProduct = it,
                    onAdd = {id->
                        events(CartEvents.OnDecrease(id)) },
                    onRemove = { id -> events(CartEvents.OnIncrease(id)) },
                    onSelect = {events(CartEvents.OnSelect(it))},
                    isUpdating = state.isUpdating == it.cart.id ,
                    isSelected = state.selectedCartId.contains(it.cart.id)

                )
            }
        }
        HorizontalDivider()
        Row(
            modifier = modifier.fillMaxWidth().height(50.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val current = state.carts.size == state.selectedCartId.size
            Checkbox(
                checked = current,
                onCheckedChange = {events(CartEvents.OnSelectAll(current))}
            )
            Column(
                modifier = modifier.fillMaxWidth().weight(1f).padding(2.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                val filterAllSelectedCarts = state.carts.filter { it.cart.id in state.selectedCartId }
                Text(
                    state.total.toPhp(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Description(
                    description = "${filterAllSelectedCarts.getAllQuantity()} items"
                )
            }

            Spacer(modifier.width(8.dp))
            Box(
                modifier = modifier
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(
                        horizontal = 16.dp,
                    ).clickable {
                        events(CartEvents.OnCheckout(context = context, navHostController = navHostController))
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    "Checkout",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun CartItem(
    modifier: Modifier = Modifier,
    cartWithProduct: CartWithProduct,
    isUpdating : Boolean,
    isSelected : Boolean,
    onSelect : (String) -> Unit,
    onAdd : (String) -> Unit,
    onRemove : (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = {onSelect(cartWithProduct.cart.id!!)}
        )
        RoundedImage(
           image =  cartWithProduct.product.image ?: "",
            modifier = modifier.size(80.dp)
        )
        Column {
            Text(
                "${cartWithProduct.product.name}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier.height(4.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    cartWithProduct.product.price.toPhp(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable(
                                enabled = !isUpdating && cartWithProduct.cart.quantity!! > 1,
                                onClick = {
                                    onRemove(cartWithProduct.cart.id ?: "")
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Filled.Remove,
                            contentDescription = "Remove",
                            tint = Color.White // Adjust the color for visibility
                        )
                    }

                    // Display quantity in between the boxes
                    Text(
                        text = "${cartWithProduct.cart.quantity}",
                       style = MaterialTheme.typography.titleMedium.copy(
                           fontWeight = FontWeight.Bold
                       )
                    )

                    // Increase quantity button (add icon)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                            .clickable(
                                enabled = !isUpdating && cartWithProduct.cart.quantity!! < cartWithProduct.product.quantity!!,
                                onClick = {
                                    onAdd(cartWithProduct.cart.id ?: "")
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(12.dp),
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = Color.White // Adjust the color for visibility
                        )
                    }
                }
            }
        }
    }
}