package com.feragusper.taximeter.libraries.location.domain.repository

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import kotlinx.coroutines.flow.Flow

/**
 * Repository to fetch location updates.
 */
interface LocationTrackingRepository {

    /**
     * Get location updates.
     *
     * @return [Flow] of [LocationPoint] with the location updates.
     */
    fun getLocationUpdates(): Flow<LocationPoint>
}
