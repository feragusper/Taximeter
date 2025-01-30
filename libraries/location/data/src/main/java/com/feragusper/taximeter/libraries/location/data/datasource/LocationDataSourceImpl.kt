package com.feragusper.taximeter.libraries.location.data.datasource

import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [LocationDataSource] using Ktor and Kotlin Serialization for HTTP requests.
 */
class LocationDataSourceImpl() : LocationDataSource {

    /**
     * Get location updates.
     *
     * @return [Flow] of [LocationPointEntity] with the location updates.
     */
    override fun getLocationUpdates(): Flow<LocationPointEntity> {
        return LocationProvider.getRouteFlow()
    }

}
