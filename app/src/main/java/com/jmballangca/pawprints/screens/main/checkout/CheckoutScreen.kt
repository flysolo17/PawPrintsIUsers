package com.jmballangca.pawprints.screens.main.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TopAppBar


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.models.transaction.PaymentType
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.transaction.TransactionItems
import com.jmballangca.pawprints.models.transaction.TransactionType
import com.jmballangca.pawprints.utils.Description
import com.jmballangca.pawprints.utils.DetailRow
import com.jmballangca.pawprints.utils.RoundedImage
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.getItemTotalPrice
import com.jmballangca.pawprints.utils.getItemsTotalQuantity
import com.jmballangca.pawprints.utils.toPhp
import com.jmballangca.pawprints.utils.toast
import kotlinx.coroutines.delay


@Composable
fun CheckoutScreen(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    state: CheckoutState,
    events: (CheckoutEvents) -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    LaunchedEffect(state) {

        if (state.transactionSubmitted != null) {
            context.toast(
                state.transactionSubmitted
            )
            delay(1000)
            navHostController.popBackStack()
        }
        if (state.errors != null) {
            context.toast(state.errors)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = modifier.fillMaxSize().weight(1f).verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                title = { Text("Confirm Transaction") },
                navigationIcon = { IconButton(onClick = { navHostController.popBackStack()}) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "back")
                }}
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Store,
                    contentDescription = "Store",
                    modifier=modifier.size(46.dp)
                )
                Column {
                    Text(text = "Aucena Veterinary Clinic", style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ))
                    Description(
                        description = "Bonifacio San Jose Occidental Mindoro"
                    )
                }
            }
            HorizontalDivider()
            ItemInfo(
                items = transaction.items
            )
        }
        Column(
           modifier = modifier.fillMaxWidth().padding(8.dp)
        ) {
            HorizontalDivider()
            PaymentDetailsInfo(
                items = transaction.items
            )
            Button(
                modifier = modifier.fillMaxWidth().padding(8.dp),
                enabled = !state.isLoading,
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    events(CheckoutEvents.OnSubmit(transaction))
                }
            ) {
                Box(
                    modifier = modifier.fillMaxWidth().padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) {
                        Row {
                            CircularProgressIndicator(
                                modifier = modifier.size(24.dp)
                            )
                            Spacer(
                                modifier = modifier.width(4.dp)
                            )
                            Text("Creating transaction...")
                        }
                    } else {
                        Text("Checkout")
                    }

                }
            }
        }

    }
}

@Composable
fun PaymentDetailsInfo(
    modifier: Modifier = Modifier,
    items: List<TransactionItems>
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {

        Text(
            "Payment Details",
            style = MaterialTheme.typography.titleMedium
        )

        DetailRow(
            label = "Total Quantity",
            value = "${items.getItemsTotalQuantity()}"
        )
        DetailRow(
            label = "Payment Type",
            value = "${PaymentType.CASH}"
        )
        DetailRow(
            label = "Transaction Type",
            value = "${TransactionType.PICK_UP}"
        )

        DetailRow(
            label = "Total",
            value = "${items.getItemTotalPrice().toPhp()}"
        )
    }
}

@Composable
fun ItemInfo(
    modifier: Modifier = Modifier,
    items: List<TransactionItems>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = modifier.fillMaxWidth().padding(8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Items", style = MaterialTheme.typography.titleMedium)
                Text(text = "${items.size}", style = MaterialTheme.typography.titleMedium)
            }
            HorizontalDivider()
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items.forEach {
                    ItemCard(items=it)
                }
                DetailRow(
                    label = "Sub Total",
                    value = "${items.getItemTotalPrice().toPhp()}"
                )
            }
        }
    }
}



@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    items: TransactionItems,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            items.imageUrl,
            contentDescription = "${items.name}",
            modifier = modifier.size(80.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Gray),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.paw_logo),
            error = painterResource(R.drawable.paw_logo)
        )
        Column {
            Text(
                "${items.name}",
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
                    (items.price.toPhp()),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Description(
                    description = "qty ${items.quantity}",
                )
            }
        }
    }
}
