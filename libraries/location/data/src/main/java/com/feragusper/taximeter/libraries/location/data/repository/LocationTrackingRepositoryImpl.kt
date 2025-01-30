package com.feragusper.taximeter.libraries.location.data.repository

import com.feragusper.taximeter.libraries.location.data.datasource.LocationDataSource
import com.feragusper.taximeter.libraries.location.data.datasource.LocationPointEntity
import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.location.domain.repository.LocationTrackingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository to fetch location updates.
 */
class LocationTrackingRepositoryImpl(private val locationDataSource: LocationDataSource) :
    LocationTrackingRepository {

    /**
     * Get location updates.
     *
     * @return [Flow] of [LocationPoint] with the location updates.
     */
    override fun getLocationUpdates(): Flow<LocationPoint> {
        return locationDataSource.getLocationUpdates().map { it.toLocationPoint() }
    }

    /**
     * Map a [LocationPointEntity] to a [LocationPoint].
     */
    private fun LocationPointEntity.toLocationPoint() = LocationPoint(
        lat = lat,
        lon = long,
    )
}
