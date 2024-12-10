package com.jmballangca.pawprints.screens.main.products.view_product

import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jmballangca.pawprints.R
import com.jmballangca.pawprints.models.pets.Pet
import com.jmballangca.pawprints.models.products.ProductType
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.screens.main.pets.PetEvents
import com.jmballangca.pawprints.screens.main.pets.components.SelectPetBottomDialog
import com.jmballangca.pawprints.screens.main.products.ProductScreen
import com.jmballangca.pawprints.utils.Description
import com.jmballangca.pawprints.utils.ProgressBar
import com.jmballangca.pawprints.utils.Title
import com.jmballangca.pawprints.utils.UnknownError
import com.jmballangca.pawprints.utils.toPhp
import com.jmballangca.pawprints.utils.toast
import kotlinx.coroutines.launch


@Composable
fun ViewProductScreen(
    modifier: Modifier = Modifier,
    productID : String,
    state: ViewProductState,
    events : (ViewProductEvents) -> Unit,
    navHostController: NavHostController
) {
    LaunchedEffect(productID) {
        events(ViewProductEvents.OnGetProduct(productID))
    }

    when {
        state.isLoading -> ProgressBar(
            title = "Getting Product.."
        )
        state.isGettingPets -> ProgressBar(
            title = "Getting pets.."
        )
        state.errors != null -> UnknownError(
            title = state.errors
        ) {
            TextButton(onClick = {navHostController.popBackStack()}) {
                Text("back")
            }
        } else -> {
            ProductLayout(
                state = state,
                events = events,
                onBackPressed = {navHostController.popBackStack()},
                navHostController = navHostController
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductLayout(
    modifier: Modifier = Modifier,
    state: ViewProductState,
    events: (ViewProductEvents) -> Unit,
    onBackPressed : () -> Unit,
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val product = state.product
    LaunchedEffect(state){
        if (state.cartAdded != null) {
            context.toast(state.cartAdded)
        }
    }
    if (product == null) {
        UnknownError(
            title = "Product not found!"
        ) {
            TextButton(
                onClick = onBackPressed
            ) {
                Text("Back")
            }
        }
    } else {
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        if (showBottomSheet) {
            SelectPetBottomDialog(
                pets = state.pets,
                sheetState = sheetState,
                onConfirm = {},
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            )
        }

        Column(
            modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = modifier.wrapContentSize(),
                contentAlignment = Alignment.TopStart
            ) {
                AsyncImage(
                    model = product.image!!,
                    contentDescription = product.name,
                    error = painterResource(R.drawable.paw_logo),
                    placeholder = painterResource(R.drawable.paw_logo),
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = 16.dp,
                                bottomStart = 16.dp
                            )
                        )
                )
                FilledIconButton(
                    modifier = modifier.padding(8.dp),
                    onClick = {onBackPressed()}
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Title(title = product.name ?: "No name", modifier = modifier.fillMaxWidth().weight(1f))
                Text(
                    product.price.toPhp(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    "Description",
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Description(description = product.description ?: "no description")
                Text(
                    "Content",
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Description(description = product.contents ?: "no description")
                Text(
                    "Features",
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Description(description = product.features ?: "no description")
            }
            if(product.type == ProductType.SERVICES) {
                Button(
                    modifier = modifier.fillMaxWidth().padding(8.dp),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        navHostController.navigate(AppRouter.CreateAppointmentRoute.navigate(product))
                    }
                ) {
                    Text(
                        "Book an appointment",
                        modifier = modifier.padding(8.dp)
                    )
                }
            } else {
                Row(
                    modifier = modifier.fillMaxSize().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        modifier = modifier.fillMaxWidth().weight(1f),
                        onClick = {
                            events.invoke(ViewProductEvents.OnAddToCart(state.product))
                        },
                        enabled = !state.adding
                    ) {
                        if (state.adding) {
                            CircularProgressIndicator()
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ShoppingCart,
                                    contentDescription = "Cart"
                                )
                                Text("Add to cart")
                            }
                        }


                    }
                    Button(
                        shape = MaterialTheme.shapes.small,
                        modifier = modifier.fillMaxWidth().weight(1f),
                        onClick = {
                            events(ViewProductEvents.OnCheckout(navHostController, context))
                        }
                    ) {
                        Text("Buy now")
                    }
                }
            }
        }
    }
}