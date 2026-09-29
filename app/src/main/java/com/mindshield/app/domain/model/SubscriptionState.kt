package com.mindshield.app.domain.model

sealed class SubscriptionState {
    object Free : SubscriptionState()
    data class Premium(val expiryDate: Long) : SubscriptionState()
    data class Trial(val daysRemaining: Int) : SubscriptionState()
}
