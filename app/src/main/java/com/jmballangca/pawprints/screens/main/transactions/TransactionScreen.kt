package com.jmballangca.pawprints.screens.main.transactions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.models.transaction.Payment
import com.jmballangca.pawprints.models.transaction.PaymentType
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.transaction.TransactionItems
import com.jmballangca.pawprints.models.transaction.TransactionStatus
import com.jmballangca.pawprints.screens.main.inbox.InboxEvents
import com.jmballangca.pawprints.ui.theme.PawPrintsTheme
import com.jmballangca.pawprints.utils.DetailRow
import com.jmballangca.pawprints.utils.toPhp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    state: TransactionState,
    events: (TransactionEvents) -> Unit,
    navHostController: NavHostController,
) {

    LaunchedEffect(state.user?.id) {
        if (state.user != null) {
            state.user?.id?.let {
                events(TransactionEvents.OnGetMyTransactions(it))
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text("Transactions") },
                navigationIcon = { IconButton(onClick = {navHostController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(it)
        ){
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
            items(state.transactions) {
                TransactionCard(transaction = it)
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    transaction: Transaction
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Status: ${transaction.status.name}",
                style = MaterialTheme.typography.labelSmall,
            )
            Column(
            ) {
                transaction.items.forEach { item ->
                    ListItem(
                        text = { Text("${item.name.toString()}") },
                        icon = {
                            AsyncImage(
                                model = item.imageUrl!!,
                                contentDescription = item.name,
                                error = painterResource(R.drawable.paw_logo),
                                placeholder = painterResource(R.drawable.paw_logo),
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .size(60.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            bottomEnd = 16.dp,
                                            bottomStart = 16.dp
                                        )
                                    )
                            )
                        },
                        secondaryText = {
                            Row(
                                modifier = modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Text(
                                    text = ((item.quantity ?: 0) * (item.price ?: 0.0)).toPhp(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = "${item.quantity}",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            DetailRow(
                label = "Total",
                value = "${transaction.payment?.total?.toPhp()}"
            )
        }
    }
}
                @Preview
@Composable
private fun TransactionCardPrev() {
    PawPrintsTheme {
        val transactionSample = Transaction(
            id = "TX12345",
            userID = "USER123",
            items = listOf(
                TransactionItems(
                    productID = "ITEM001",
                    name = "Dog Food",
                    quantity = 2,
                    price = 50.0,
                    imageUrl = "https://firebasestorage.googleapis.com/v0/b/pawprints-40b61.appspot.com/o/products%2F415001737155%2F1000119565?alt=media&token=50a0d7d2-f858-45f1-ad90-2f18ddb82578"
                ),
                TransactionItems(
                    productID = "ITEM002",
                    name = "Cat Toy",
                    imageUrl = "https://firebasestorage.googleapis.com/v0/b/pawprints-40b61.appspot.com/o/products%2F415001737155%2F1000119565?alt=media&token=50a0d7d2-f858-45f1-ad90-2f18ddb82578",
                    quantity = 1,
                    price = 15.0
                )
            ),
            payment = Payment(
                type = PaymentType.CASH,
                total = 115.0
            ),
            createdAt = Date(),
            updatedAt = Date()
        )

        TransactionCard(
            transaction =  transactionSample
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun TransactionScreenPrev() {
    PawPrintsTheme {
        TransactionScreen(
            state = TransactionState(),
            events = {},
            navHostController = rememberNavController()
        )
    }
}