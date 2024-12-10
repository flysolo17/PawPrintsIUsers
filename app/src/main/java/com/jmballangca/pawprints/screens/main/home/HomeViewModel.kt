package com.jmballangca.pawprints.screens.main.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.models.cart.Cart
import com.jmballangca.pawprints.models.products.Product
import com.jmballangca.pawprints.repository.appointment.AppointmentRepository
import com.jmballangca.pawprints.repository.cart.CartRepository
import com.jmballangca.pawprints.repository.products.ProductRepository
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.generateRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val appointmentRepository : AppointmentRepository,
)  : ViewModel() {
    var state by mutableStateOf(HomeState())
    init {
        events(HomeEvents.OnGetAllProducts)
    }
    fun events(e : HomeEvents) {
        when (e) {
            HomeEvents.OnGetAllProducts -> getProducts()
            is HomeEvents.OnSetUser -> state = state.copy(users = e.users)
            is HomeEvents.AddToCart -> addToCart(e.product)
            is HomeEvents.OnTabSelected -> state = state.copy(
                selectedTab = e.tab
            )

            is HomeEvents.OnGetAppointments -> getAppointments(e.userID)
        }
    }

    private fun getAppointments(userID: String) {
        viewModelScope.launch {
            appointmentRepository.getMyAppointments(userID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isGettingAppointments = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isGettingAppointments = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isGettingAppointments = false,
                        errors = null,
                        appointments = it.data
                    )
                }
            }
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
                        adding = null,
                        cartAdded = null
                    )
                    UiState.Loading -> state.copy(
                        adding = product.id,
                        errors = null,
                        cartAdded = null
                    )
                    is UiState.Success -> state.copy(
                        adding = null,
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

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts {
                state = when(it) {
                    is UiState.Error ->
                        state.copy(
                            isLoading = false,
                            errors = it.message
                        )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        products = it.data
                    )
                }
            }

        }
    }
}