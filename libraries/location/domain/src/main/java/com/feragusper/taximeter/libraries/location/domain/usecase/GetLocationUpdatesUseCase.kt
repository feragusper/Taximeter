package com.feragusper.taximeter.libraries.location.domain.usecase

import com.feragusper.taximeter.libraries.location.domain.model.LocationPoint
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get location updates.
 */
interface GetLocationUpdatesUseCase {

    /**
     * Get location updates.
     *
     * @return [Flow] of [LocationPoint] with the location updates.
     */
    operator fun invoke(): Flow<LocationPoint>
}