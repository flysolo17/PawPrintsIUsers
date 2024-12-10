package com.jmballangca.pawprints.screens.main.products.view_product

import android.content.Context
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.cart.Cart
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.models.transaction.Payment
import com.jmballangca.pawprints.models.transaction.PaymentType
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.transaction.TransactionItems
import com.jmballangca.pawprints.models.transaction.toTransactionItems
import com.jmballangca.pawprints.repository.cart.CartRepository
import com.jmballangca.pawprints.repository.pets.PetRepository
import com.jmballangca.pawprints.repository.products.ProductRepository
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.computeAll
import com.jmballangca.pawprints.utils.generateRandomNumberString
import com.jmballangca.pawprints.utils.generateRandomString
import com.jmballangca.pawprints.utils.getItemTotalPrice
import com.jmballangca.pawprints.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val petRepository: PetRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    var state by mutableStateOf(ViewProductState())
    fun events(e : ViewProductEvents) {
        when(e) {
            is ViewProductEvents.OnGetProduct -> getProduct(e.productID)
            is ViewProductEvents.OnGetMyPets -> getPets(
                ownerID = e.uid)
            is ViewProductEvents.OnSetUsers -> state = state.copy(
                users = e.users
            )

            is ViewProductEvents.OnAddToCart -> addToCart(e.product)
            is ViewProductEvents.OnCheckout -> checkout(e.navHostController,e.context)
        }
    }

    private fun addToCart(product: Product) {
        val cart : Cart = Cart(
            id = generateRandomString(),
            userID = state.users?.id,
            productID = product.id,
            quantity = 1,
            price = product.price
        )
        viewModelScope.launch {
            cartRepository.addToCart(cart) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        errors = it.message,
                        adding = false,
                        cartAdded = null
                    )
                    UiState.Loading -> state.copy(
                        adding =true,
                        errors = null,
                        cartAdded = null
                    )
                    is UiState.Success -> state.copy(
                        adding = false,
                        cartAdded = it.data
                    )
                }
            }
            delay(1000)
            state = state.copy(
                cartAdded = null
            )
        }
    }

    private fun checkout(navHostController: NavHostController, context: Context) {
        val product  : TransactionItems =    TransactionItems(
            productID = state.product?.id,
            name = state.product?.name,
            imageUrl = state.product?.image,
            quantity =  1,
            price = state.product?.price
        )
        val items = listOf(product)
        val transaction : Transaction = Transaction(
            id = generateRandomNumberString(12),
            userID = state.users?.id,
            items = items,
            payment = Payment(
                id = generateRandomNumberString(8),
                type = PaymentType.CASH,
                total = items.getItemTotalPrice()
            )
        )
        navHostController.navigate(AppRouter.CheckoutPage.navigate(transaction))
    }

    private fun getPets(ownerID: String) {
        viewModelScope.launch {
            petRepository.getAllMyPet(ownerID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isGettingPets = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isGettingPets = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isGettingPets = false,
                        errors = null,
                        pets = it.data
                    )
                }
            }
        }
    }

    private fun getProduct(productID: String) {
        viewModelScope.launch {
            productRepository.getProductByID(productID) {
                when(it) {
                    is UiState.Error ->state =  state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading ->state =  state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> {
                        state =   state.copy(
                            isLoading = false,
                            errors = null,
                            product = it.data
                        )
                        state.users?.id?.let {
                            events(ViewProductEvents.OnGetMyPets(it))
                        }
                    }
                }
            }
        }
    }
}