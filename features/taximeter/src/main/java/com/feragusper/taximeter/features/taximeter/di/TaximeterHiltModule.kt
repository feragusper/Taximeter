package com.feragusper.taximeter.features.taximeter.di

import com.feragusper.taximeter.features.taximeter.TaximeterEffectProducer
import com.feragusper.taximeter.features.taximeter.TaximeterIntentProcessor
import com.feragusper.taximeter.features.taximeter.TaximeterStateMapper
import com.feragusper.taximeter.libraries.ride.domain.RideUpdateCoordinator
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideFareSummaryUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetSupplementsUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.StartRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.UpdateRideSupplementsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal class TaximeterHiltModule {
    @ViewModelScoped
    @Provides
    fun providesStateMapper() = TaximeterStateMapper()

    @ViewModelScoped
    @Provides
    fun providesEffectProducer() = TaximeterEffectProducer()

    @ViewModelScoped
    @Provides
    fun providesIntentProcessor(
        getCurrentRideUpdatesUseCase: GetCurrentRideUpdatesUseCase,
        startRideUseCase: StartRideUseCase,
        rideUpdateCoordinator: RideUpdateCoordinator,
        getSupplementsUseCase: GetSupplementsUseCase,
        updateRideSupplementsUseCase: UpdateRideSupplementsUseCase,
        getCurrentRideFareSummaryUseCase: GetCurrentRideFareSummaryUseCase
    ) = TaximeterIntentProcessor(
        getCurrentRideUpdatesUseCase = getCurrentRideUpdatesUseCase,
        startRideUseCase = startRideUseCase,
        rideUpdateCoordinator = rideUpdateCoordinator,
        getSupplementsUseCase = getSupplementsUseCase,
        updateRideSupplementsUseCase = updateRideSupplementsUseCase,
        getCurrentRideFareSummaryUseCase = getCurrentRideFareSummaryUseCase
    )
}
