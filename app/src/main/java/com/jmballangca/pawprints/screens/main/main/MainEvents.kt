package com.jmballangca.pawprints.screens.main.main



sealed interface MainEvents {
    data object OnGetCurrentUser : MainEvents
    data class OnGetMyCart(val uid : String) : MainEvents
}