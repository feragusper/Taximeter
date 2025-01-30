package com.feragusper.taximeter.libraries.location.data.usecase

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import com.feragusper.taximeter.libraries.location.domain.repository.LocationTrackingRepository
import com.feragusper.taximeter.libraries.location.domain.usecase.GetLocationUpdatesUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get location updates.
 */
class GetLocationUpdatesUseCaseImpl(
    private val locationTrackingRepository: LocationTrackingRepository
) : GetLocationUpdatesUseCase {

    /**
     * Get location updates.
     *
     * @return [Flow] of [LocationPoint] with the location updates.
     */
    override operator fun invoke(
    ): Flow<LocationPoint> {
        return locationTrackingRepository.getLocationUpdates()
    }
}
