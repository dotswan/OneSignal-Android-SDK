package com.onesignal.onesignal.location.internal.capture.impl

import android.location.Location
import com.onesignal.onesignal.core.internal.application.IApplicationService
import com.onesignal.onesignal.core.internal.logging.Logging
import com.onesignal.onesignal.core.internal.time.ITime
import com.onesignal.onesignal.core.internal.user.IUserSwitcher
import com.onesignal.onesignal.location.internal.capture.ILocationCapturer
import com.onesignal.onesignal.location.internal.common.LocationPoint
import com.onesignal.onesignal.location.internal.controller.ILocationController
import com.onesignal.onesignal.location.internal.controller.ILocationUpdatedHandler
import com.onesignal.onesignal.location.internal.preferences.ILocationPreferencesService
import java.math.BigDecimal
import java.math.RoundingMode

internal class LocationCapturer (
    private val _applicationService: IApplicationService,
    private val _time: ITime,
    private val _prefs: ILocationPreferencesService,
    private val _userSwitcher: IUserSwitcher,
    private val _controller: ILocationController,
) : ILocationUpdatedHandler, ILocationCapturer {

    override var locationCoarse: Boolean = false

    init {
        _controller.subscribe(this)
    }

    override fun captureLastLocation() {
        val location = _controller.getLastLocation()

        if(location != null) {
            capture(location)
        }
        else {
            _prefs.lastLocationTime = _time.currentTimeMillis
        }
    }

    override fun onLocationChanged(location: Location) {
        Logging.debug("LocationController fireCompleteForLocation with location: $location")
        capture(location)
    }

    private fun capture(location: Location) {
        val point = LocationPoint()

        point.accuracy = location.accuracy
        point.bg = !_applicationService.isInForeground
        point.type = if (locationCoarse) 0 else 1
        point.timeStamp = location.time

        // Coarse always gives out 14 digits and has an accuracy 2000.
        // Always rounding to 7 as this is what fine returns.
        if (locationCoarse) {
            point.lat = BigDecimal(location.latitude).setScale(7, RoundingMode.HALF_UP).toDouble()
            point.log = BigDecimal(location.longitude).setScale(7, RoundingMode.HALF_UP).toDouble()
        } else {
            point.lat = location.latitude
            point.log = location.longitude
        }

        var userProperties = _userSwitcher.propertiesModel
        userProperties.locationLongitude = point.log
        userProperties.locationLatitude = point.lat
        userProperties.locationAccuracy = point.accuracy
        userProperties.locationBackground = point.bg
        userProperties.locationType = point.type
        userProperties.locationTimestamp = point.timeStamp
        _prefs.lastLocationTime = _time.currentTimeMillis
    }
}