package com.onesignal.user.internal.backend.impl

import com.onesignal.common.exceptions.BackendException
import com.onesignal.common.safeJSONObject
import com.onesignal.core.internal.http.IHttpClient
import com.onesignal.user.internal.backend.ISubscriptionBackendService
import com.onesignal.user.internal.backend.SubscriptionObject
import org.json.JSONObject

internal class SubscriptionBackendService(
    private val _httpClient: IHttpClient
) : ISubscriptionBackendService {

    override suspend fun createSubscription(appId: String, aliasLabel: String, aliasValue: String, subscription: SubscriptionObject): String {
        val requestJSON = JSONObject()
            .put("subscription", JSONConverter.convertToJSON(subscription))
            .put("retain_previous_owner", true)

        val response = _httpClient.post("apps/$appId/users/by/$aliasLabel/$aliasValue/subscriptions", requestJSON)

        if (!response.isSuccess) {
            // TODO: Temporary work around code until resolved on backend
            if (response.statusCode == 500 && response.payload != null) {
                val payload = JSONObject(response.payload!!)
                if (payload.getBoolean("success") || payload.getJSONObject("subscription").has("id")) {
                    val responseJSON = JSONObject(response.payload!!)
                    val subscriptionJSON = responseJSON.safeJSONObject("subscription")
                    if (subscriptionJSON == null || !subscriptionJSON.has("id")) {
                        throw BackendException(response.statusCode, response.payload)
                    }

                    return subscriptionJSON.getString("id")
                }
            } else if (response.statusCode == 400 && response.payload != null) {
                val errors = JSONObject(response.payload!!)
                    .getJSONArray("errors")
                if (errors.length() > 0 && errors.getJSONObject(0).getString("title").contains("Subscription already belongs to target user")) {
                    throw BackendException(409, response.payload)
                }
            }
            // TODO: End Temporary work around code until resolved on backend
            throw BackendException(response.statusCode, response.payload)
        }

        val responseJSON = JSONObject(response.payload!!)
        val subscriptionJSON = responseJSON.safeJSONObject("subscription")
        if (subscriptionJSON == null || !subscriptionJSON.has("id")) {
            throw BackendException(response.statusCode, response.payload)
        }

        return subscriptionJSON.getString("id")
    }

    override suspend fun updateSubscription(appId: String, subscriptionId: String, subscription: SubscriptionObject) {
        val requestJSON = JSONObject()
            .put("subscription", JSONConverter.convertToJSON(subscription))

        val response = _httpClient.patch("apps/$appId/subscriptions/$subscriptionId", requestJSON)

        if (!response.isSuccess) {
            throw BackendException(response.statusCode, response.payload)
        }
    }

    override suspend fun deleteSubscription(appId: String, subscriptionId: String) {
        val response = _httpClient.delete("apps/$appId/subscriptions/$subscriptionId")

        if (!response.isSuccess) {
            throw BackendException(response.statusCode, response.payload)
        }
    }
}