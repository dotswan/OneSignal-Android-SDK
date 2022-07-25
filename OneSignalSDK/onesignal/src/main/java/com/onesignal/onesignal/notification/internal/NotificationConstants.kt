package com.onesignal.onesignal.notification.internal

object NotificationConstants {
    const val DEFAULT_TTL_IF_NOT_IN_PAYLOAD = 259_200


    const val PUSH_ADDITIONAL_DATA_KEY = "a"

    const val GOOGLE_SENT_TIME_KEY = "google.sent_time"
    const val GOOGLE_TTL_KEY = "google.ttl"

    const val HMS_TTL_KEY = "hms.ttl"
    const val HMS_SENT_TIME_KEY = "hms.sent_time"

    const val GENERATE_NOTIFICATION_BUNDLE_KEY_ACTION_ID = "actionId";



    const val BUNDLE_KEY_ANDROID_NOTIFICATION_ID = "androidNotificationId"

    // Bundle key the whole OneSignal payload will be placed into as JSON and attached to the
    //   notification Intent.
    const val BUNDLE_KEY_ONESIGNAL_DATA = "onesignalData"
}