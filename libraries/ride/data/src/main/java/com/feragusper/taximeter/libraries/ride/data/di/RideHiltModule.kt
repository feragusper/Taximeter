package com.feragusper.taximeter.libraries.ride.data.di

import com.feragusper.taximeter.libraries.location.domain.usecase.GetLocationUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.data.datasource.PriceDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.PriceDataSourceImpl
import com.feragusper.taximeter.libraries.ride.data.datasource.RideDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.RideDataSourceImpl
import com.feragusper.taximeter.libraries.ride.data.datasource.SupplementDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.SupplementDataSourceImpl
import com.feragusper.taximeter.libraries.ride.data.repository.PriceRepositoryImpl
import com.feragusper.taximeter.libraries.ride.data.repository.RideRepositoryImpl
import com.feragusper.taximeter.libraries.ride.data.repository.SupplementRepositoryImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.AddLocationPointToCurrentRideUseCaseImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.GetCurrentRideFareSummaryUseCaseImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.GetCurrentRideUpdatesUseCaseImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.GetSupplementsUseCaseImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.RefreshRideStateUseCaseImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.StartRideUseCaseImpl
import com.feragusper.taximeter.libraries.ride.data.usecase.UpdateRideSupplementsUseCaseImpl
import com.feragusper.taximeter.libraries.ride.domain.RideUpdateCoordinator
import com.feragusper.taximeter.libraries.ride.domain.repository.PriceRepository
import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import com.feragusper.taximeter.libraries.ride.domain.repository.SupplementRepository
import com.feragusper.taximeter.libraries.ride.domain.usecase.AddLocationPointToCurrentRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideFareSummaryUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetSupplementsUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.RefreshRideStateUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.StartRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.UpdateRideSupplementsUseCase
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
internal class RideHiltModule {

    /**
     * Provides a singleton instance of [PriceDataSource].

     * @return An instance of [PriceDataSource].
     */
    @Provides
    @Singleton
    fun providePriceDataSource(): PriceDataSource {
        return PriceDataSourceImpl()
    }

    /**
     * Provides a singleton instance of [SupplementDataSource].
     *
     * @return An instance of [SupplementDataSource].
     */
    @Provides
    @Singleton
    fun provideSupplementDataSource(): SupplementDataSource {
        return SupplementDataSourceImpl()
    }

    /**
     * Provides a singleton instance of [RideDataSource].
     *
     * @return An instance of [RideDataSource].
     */
    @Provides
    @Singleton
    fun provideRideDataSource(): RideDataSource {
        return RideDataSourceImpl()
    }

    /**
     * Provides a singleton instance of [RideUpdateCoordinator]
     *
     * @param addLocationPointToCurrentRideUseCase Use case to add location points to the ride.
     * @param refreshRideStateUseCase Use case to refresh the ride state.
     *
     * @return An instance of [RideUpdateCoordinator].
     */
    @Provides
    @Singleton
    fun provideRideUpdateCoordinator(
        addLocationPointToCurrentRideUseCase: AddLocationPointToCurrentRideUseCase,
        refreshRideStateUseCase: RefreshRideStateUseCase,
        getLocationUpdatesUseCase: GetLocationUpdatesUseCase,
    ): RideUpdateCoordinator {
        return RideUpdateCoordinator(
            addLocationPointToCurrentRideUseCase = addLocationPointToCurrentRideUseCase,
            refreshRideStateUseCase = refreshRideStateUseCase,
            getLocationUpdatesUseCase = getLocationUpdatesUseCase,
        )
    }

    /**
     * Provides a singleton instance of [RideRepository].
     *
     * @param rideDataSource The data source for ride data.
     * @param supplementDataSource The data source for supplement data.
     * @return Implementation of [RideRepository].
     */
    @Singleton
    @Provides
    fun providesRideRepository(
        rideDataSource: RideDataSource,
        supplementDataSource: SupplementDataSource
    ): RideRepository =
        RideRepositoryImpl(
            rideDataSource = rideDataSource,
            supplementDataSource = supplementDataSource
        )

    /**
     * Provides a singleton instance of [PriceRepository].
     *
     * @param priceDataSource The data source for pricing data.
     * @return Implementation of [PriceRepository].
     */
    @Singleton
    @Provides
    fun providesPriceRepository(priceDataSource: PriceDataSource): PriceRepository =
        PriceRepositoryImpl(priceDataSource)

    /**
     * Provides a singleton instance of [SupplementRepository].
     *
     * @return Implementation of [SupplementRepository].
     */
    @Singleton
    @Provides
    fun providesSupplementRepository(supplementDataSource: SupplementDataSource): SupplementRepository =
        SupplementRepositoryImpl(supplementDataSource)

    /**
     * Provides an instance of [GetCurrentRideUpdatesUseCase].
     *
     * @param rideRepository The repository handling ride data.
     * @return Implementation of [GetCurrentRideUpdatesUseCase].
     */
    @Provides
    fun providesGetCurrentRideUpdatesUseCase(
        rideRepository: RideRepository
    ): GetCurrentRideUpdatesUseCase = GetCurrentRideUpdatesUseCaseImpl(rideRepository)

    /**
     * Provides an instance of [StartRideUseCase].
     *
     * @param rideRepository The repository handling ride data.
     * @param priceRepository The repository handling pricing data.
     * @return Implementation of [StartRideUseCase].
     */
    @Provides
    fun providesStartRideUseCase(
        rideRepository: RideRepository,
        priceRepository: PriceRepository
    ): StartRideUseCase = StartRideUseCaseImpl(
        rideRepository = rideRepository,
        priceRepository = priceRepository
    )

    /**
     * Provides an instance of [AddLocationPointToCurrentRideUseCase].
     *
     * @param rideRepository The repository handling ride data.
     * @return Implementation of [AddLocationPointToCurrentRideUseCase].
     */
    @Provides
    fun providesUpdateRideUseCase(
        rideRepository: RideRepository
    ): AddLocationPointToCurrentRideUseCase =
        AddLocationPointToCurrentRideUseCaseImpl(rideRepository)

    /**
     * Provides an instance of [GetSupplementsUseCase].
     *
     * @param supplementRepository The repository handling supplement data.
     * @return Implementation of [GetSupplementsUseCase].
     */
    @Provides
    fun providesGetSupplementsUseCase(
        supplementRepository: SupplementRepository,
    ): GetSupplementsUseCase = GetSupplementsUseCaseImpl(supplementRepository)

    /**
     * Provides an instance of [GetCurrentRideFareSummaryUseCase].
     *
     * @param rideRepository The repository handling ride data.
     * @return Implementation of [GetCurrentRideFareSummaryUseCase].
     */
    @Provides
    fun providesGetCurrentRideFareSummaryUseCase(
        rideRepository: RideRepository
    ): GetCurrentRideFareSummaryUseCase = GetCurrentRideFareSummaryUseCaseImpl(rideRepository)

    /**
     * Provides an instance of [UpdateRideSupplementsUseCase].
     *
     * @param rideRepository The repository handling ride data.
     * @return Implementation of [UpdateRideSupplementsUseCase].
     */
    @Provides
    fun providesUpdateRideSupplementsUseCase(
        rideRepository: RideRepository
    ): UpdateRideSupplementsUseCase = UpdateRideSupplementsUseCaseImpl(rideRepository)

    /**
     * Provides an instance of [RefreshRideStateUseCase].
     *
     * @param rideRepository The repository handling ride data.
     * @return Implementation of [RefreshRideStateUseCase].
     */
    @Provides
    fun providesRefreshRideStateUseCase(
        rideRepository: RideRepository
    ): RefreshRideStateUseCase = RefreshRideStateUseCaseImpl(rideRepository)
}
