package com.feragusper.taximeter.libraries.location.data.di

import com.feragusper.taximeter.libraries.location.data.datasource.LocationDataSource
import com.feragusper.taximeter.libraries.location.data.datasource.LocationDataSourceImpl
import com.feragusper.taximeter.libraries.location.data.repository.LocationTrackingRepositoryImpl
import com.feragusper.taximeter.libraries.location.data.usecase.GetLocationUpdatesUseCaseImpl
import com.feragusper.taximeter.libraries.location.domain.repository.LocationTrackingRepository
import com.feragusper.taximeter.libraries.location.domain.usecase.GetLocationUpdatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency Injection module for the Ride feature.
 * This module provides implementations for repositories, use cases, and coordinators.
 */
@Module
@InstallIn(SingletonComponent::class)
internal class LocationHiltModule {

    /**
     * Provides a singleton instance of [LocationDataSource].
     *
     * @return Implementation of [LocationDataSource].
     */
    @Provides
    @Singleton
    fun providePriceDataSource(): LocationDataSource {
        return LocationDataSourceImpl()
    }

    /**
     * Provides a singleton instance of [LocationTrackingRepository].
     *
     * @param locationDataSource The data source responsible for location tracking operations.
     * @return Implementation of [LocationTrackingRepository].
     */
    @Singleton
    @Provides
    fun providesLocationTrackingRepository(locationDataSource: LocationDataSource): LocationTrackingRepository =
        LocationTrackingRepositoryImpl(locationDataSource)

    /**
     * Provides a singleton instance of [GetLocationUpdatesUseCase].
     *
     * @param locationTrackingRepository The repository responsible for location tracking operations.
     * @return Implementation of [GetLocationUpdatesUseCase].
     */
    @Provides
    fun providesGetLocationUpdatesUseCase(
        locationTrackingRepository: LocationTrackingRepository
    ): GetLocationUpdatesUseCase = GetLocationUpdatesUseCaseImpl(locationTrackingRepository)
}
