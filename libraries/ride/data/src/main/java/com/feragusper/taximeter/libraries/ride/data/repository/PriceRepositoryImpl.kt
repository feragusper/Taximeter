package com.feragusper.taximeter.libraries.ride.data.repository

import com.feragusper.taximeter.libraries.ride.data.datasource.PriceDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.entity.PriceConfigurationEntity
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import com.feragusper.taximeter.libraries.ride.domain.repository.PriceRepository

/**
 * Implementation of [PriceRepository] responsible for handling pricing-related operations.
 *
 * @param priceDataSource The data source that provides pricing configurations.
 */
class PriceRepositoryImpl(
    private val priceDataSource: PriceDataSource
) : PriceRepository {

    /**
     * Retrieves the current pricing configuration from the data source.
     *
     * @return Pricing configuration data.
     */
    override suspend fun getPriceConfiguration() =
        priceDataSource.getPriceConfiguration().toPriceConfiguration()

    private fun PriceConfigurationEntity.toPriceConfiguration(): PriceConfiguration =
        PriceConfiguration(
            pricePerKm = pricePerKm,
            pricePerSecond = pricePerSecond,
        )
}
