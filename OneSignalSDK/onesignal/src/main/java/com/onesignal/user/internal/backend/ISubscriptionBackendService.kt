package com.onesignal.user.internal.backend

import com.onesignal.user.internal.subscriptions.SubscriptionStatus

interface ISubscriptionBackendService {
    /**
     * Create a new subscription for the user identified by the [aliasLabel]/[aliasValue] provided. If the subscription
     * being created already exists under a different user, ownership will be transferred to this user provided.
     *
     * If there is a non-successful response from the backend, a [BackendException] will be thrown with response data.
     *
     * @param appId The ID of the OneSignal application this user exists under.
     * @param aliasLabel The alias label to retrieve the user under.
     * @param aliasValue The identifier within the [aliasLabel] that identifies the user to retrieve.
     * @param type The type of subscription to create.
     * @param enabled Whether this subscription is enabled.
     * @param address The subscription address.
     * @param status The subscription status.
     *
     * @return The ID of the subscription created.
     */
    suspend fun createSubscription(appId: String, aliasLabel: String, aliasValue: String, type: SubscriptionObjectType, enabled: Boolean, address: String, status: SubscriptionStatus): String

    /**
     * Update an existing subscription with the properties provided.
     *
     * @param appId The ID of the OneSignal application this subscription exists under.
     * @param subscriptionId The ID of the subscription to update.
     * @param type The new type of the subscription.
     * @param enabled Whether this subscription is enabled.
     * @param address The subscription address.
     * @param status The subscription status.
     */
    suspend fun updateSubscription(appId: String, subscriptionId: String, type: SubscriptionObjectType, enabled: Boolean, address: String, status: SubscriptionStatus)

    /**
     * Delete an existing subscription.
     *
     * @param appId The ID of the OneSignal application this subscription exists under.
     * @param subscriptionId The ID of the subscription to update.
     */
    suspend fun deleteSubscription(appId: String, subscriptionId: String)
}
