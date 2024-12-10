package com.jmballangca.pawprints.screens.main.checkout

import com.jmballangca.pawprints.models.transaction.Transaction
import com.jmballangca.pawprints.models.users.Users


sealed interface CheckoutEvents  {
    data class OnSubmit(val transaction : Transaction): CheckoutEvents
    data class OnSetUser(val users: Users?) : CheckoutEvents
}