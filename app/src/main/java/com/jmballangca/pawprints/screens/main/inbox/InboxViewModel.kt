package com.jmballangca.pawprints.screens.main.inbox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.repository.inbox.InboxRepository
import com.jmballangca.pawprints.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InboxViewModel @Inject constructor(
    private val inboxRepository: InboxRepository
) : ViewModel() {
    var state by mutableStateOf(InboxState())
    fun events(e : InboxEvents) {
        when(e) {
            is InboxEvents.OnGetMyInbox -> getInbox(e.uid)
            is InboxEvents.OnSetUsers -> state = state.copy(
                user = e.users
            )
        }
    }

    private fun getInbox(uid: String) {
        viewModelScope.launch {
            inboxRepository.getAllInboxByUserID(uid) {
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
                        isLoading = false, errors = null, inboxes =  it.data
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