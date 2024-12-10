package com.jmballangca.pawprints.screens.main.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.repository.transactions.TransactionRepository
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CheckoutVIewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    var state by mutableStateOf(CheckoutState())

    fun events(e : CheckoutEvents) {
        when(e) {
           is CheckoutEvents.OnSubmit -> submit(e.transaction)
            is CheckoutEvents.OnSetUser -> state = state.copy(users = e.users)
        }
    }

    private fun submit(transaction: Transaction) {
        viewModelScope.launch {
            transaction.userID = state.users?.id
            transactionRepository.createTransaction(transaction) {
                state = when(it) {
                    is UiState.Error -> state.copy(
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
                        transactionSubmitted = it.data
                    )
                }
            }
        }
    }
}