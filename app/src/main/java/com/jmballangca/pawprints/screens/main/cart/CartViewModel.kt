package com.jmballangca.pawprints.screens.main.cart

import android.content.Context
import android.text.Selection.selectAll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.jmballangca.pawprints.models.transaction.Payment
import com.jmballangca.pawprints.models.transaction.PaymentType
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.transaction.TransactionItems
import com.jmballangca.pawprints.models.transaction.TransactionStatus
import com.jmballangca.pawprints.models.transaction.TransactionType
import com.jmballangca.pawprints.models.transaction.toTransactionItems
import com.jmballangca.pawprints.repository.cart.CartRepository
import com.jmballangca.pawprints.router.AppRouter
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.computeAll
import com.jmballangca.pawprints.utils.generateRandomNumberString
import com.jmballangca.pawprints.utils.toPhp
import com.jmballangca.pawprints.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CartViewModel @Inject constructor(
    private  val cartRepository: CartRepository
) : ViewModel() {
    var state by mutableStateOf(CartState())
    fun events(e : CartEvents) {
        when(e) {
            is CartEvents.OnUpdateCart ->state = state.copy(
                carts = e.carts
            )
            is CartEvents.OnDecrease -> increase(e.id)
            is CartEvents.OnIncrease -> decrease(e.id)
            is CartEvents.OnSelect -> select(e.id)
            is CartEvents.OnSelectAll -> selectAll(e.current)
            is CartEvents.OnCheckout -> checkout(e.navHostController,e.context)
            is CartEvents.OnSetUsers -> state = state.copy(
                users = e.users
            )
        }
    }

    private fun checkout(navHostController: NavHostController, context: Context) {
        if (state.selectedCartId.isEmpty()) {
            context.toast("No selected items")
            return
        }

        val filterAllSelectedCarts = state.carts.filter { it.cart.id in state.selectedCartId }
        val transaction : Transaction = Transaction(
            id = generateRandomNumberString(12),
            userID = state.users?.id,
            items = filterAllSelectedCarts.toTransactionItems(),
            payment = Payment(
                id = generateRandomNumberString(8),
                type = PaymentType.CASH,
                total = filterAllSelectedCarts.computeAll()
            )
        )
        navHostController.navigate(AppRouter.CheckoutPage.navigate(transaction))
    }

    private fun selectAll(current: Boolean) {
        val newSelectAllState = !current
        val updatedSelectedCartIds = if (newSelectAllState) {
            state.carts.map { it.cart.id!! }
        } else {
            emptyList()
        }

        val updatedTotal = if (newSelectAllState) {
            state.carts.computeAll()
        } else {
            0.0
        }

        state = state.copy(
            selectAll = newSelectAllState,
            selectedCartId = updatedSelectedCartIds,
            total = updatedTotal
        )
    }



    private fun select(id: String) {
        val current = state.selectedCartId.toMutableList()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        val filterAllSelectedCarts = state.carts.filter { it.cart.id in current }
        state = state.copy(
            selectedCartId = current,
            total = filterAllSelectedCarts.computeAll()
        )
    }


    private fun decrease(id: String) {
        viewModelScope.launch {
            cartRepository.decreaseQuantity(id) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isUpdating = null,
                        errors = null
                    )
                    UiState.Loading ->state.copy(
                        isUpdating = id,
                        errors = null
                    )
                    is UiState.Success -> state.copy(isUpdating = null,
                            updated = it.data,
                    )

                }
            }
            delay(1000)
            val filterAllSelectedCarts = state.carts.filter { it.cart.id in state.selectedCartId }
            state = state.copy(
                updated = null,
                total = filterAllSelectedCarts.computeAll()
            )
        }
    }

    private fun increase(id: String) {
        viewModelScope.launch {
            cartRepository.increaseQuantity(id) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isUpdating = null,
                        errors = null
                    )
                    UiState.Loading ->state.copy(
                        isUpdating = id,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isUpdating = null,
                        updated = it.data)

                }
            }
            delay(1000)
            val filterAllSelectedCarts = state.carts.filter { it.cart.id in state.selectedCartId }
            state = state.copy(
                updated = null,
                total = filterAllSelectedCarts.computeAll()
            )
        }
    }


}