package com.jmballangca.pawprints.screens.main.messages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.pawprints.models.message.Message
import com.jmballangca.pawprints.repository.messaging.MessageRepository
import com.jmballangca.pawprints.utils.UiState
import com.jmballangca.pawprints.utils.generateRandomNumberString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    var state by mutableStateOf(MessageState())
    fun events(e : MessageEvents) {
        when(e) {
            is MessageEvents.OnGetMyMessage -> getMessage(e.userID)
            is MessageEvents.OnSetUsers -> state = state.copy(user = e.user)
            is MessageEvents.SendMessage -> sendMessage(e.message)
            is MessageEvents.OnMessageChange -> state = state.copy(
                message = e.message
            )
        }
    }

    private fun sendMessage(message: String) {
        viewModelScope.launch {
            val messages = Message(
                id = generateRandomNumberString(),
                sender = state.user?.id,
                message = message,
            )
            messageRepository.addMessage(message = messages) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isSending = false,
                        errors = it.message,
                    )
                    UiState.Loading -> state.copy(
                        isSending = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isSending = false,
                        errors = null,
                        messageSent = it.data,
                        message = ""
                    )
                }
            }
            delay(1000)
            state= state.copy(messageSent = null)
        }
    }

    private fun getMessage(userID: String) {
        viewModelScope.launch {
            messageRepository.getMessages(userID) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null,
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        messages = it.data
                    )
                }
            }
        }
    }
}