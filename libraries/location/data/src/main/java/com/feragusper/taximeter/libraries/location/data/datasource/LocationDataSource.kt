package com.feragusper.taximeter.libraries.location.data.datasource

import kotlinx.coroutines.flow.Flow

/**
 * Data source to fetch location updates.
 */
interface LocationDataSource {

    /**
     * Get location updates.
     *
     * @return [Flow] of [LocationPointEntity] with the location updates.
     */
    fun getLocationUpdates(): Flow<LocationPointEntity>
}
