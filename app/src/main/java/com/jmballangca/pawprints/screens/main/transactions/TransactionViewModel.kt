package com.jmballangca.pawprints.screens.main.transactions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.repository.transactions.TransactionRepository
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository : TransactionRepository
) : ViewModel() {
    var state by mutableStateOf(TransactionState())
    fun events(e : TransactionEvents) {
        when(e) {
            is TransactionEvents.OnGetMyTransactions -> getMyTransactions(e.userID)
            is TransactionEvents.OnSetUsers -> state = state.copy(user = e.users)
        }
    }

    private fun getMyTransactions(userID: String) {
        viewModelScope.launch {
            transactionRepository.getALlMyTransactions(userID) {
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
                        transactions =  it.data
                    )
                }
            }
            delay(
                1000
            )
            state = state.copy(
                errors = null
            )
        }
    }
}