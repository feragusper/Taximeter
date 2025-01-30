package com.feragusper.taximeter.libraries.ride.data.datasource

import com.feragusper.taximeter.libraries.ride.data.datasource.entity.PriceConfigurationEntity

/**
 * Data source interface for retrieving price configuration details.
 *
 * This interface defines the contract for fetching pricing configurations
 * which include distance-based pricing, time-based pricing, and additional
 * fees such as supplements.
 */
interface PriceDataSource {

    /**
     * Retrieves the current price configuration.
     *
     * @return [PriceConfigurationEntity] containing the pricing details.
     */
    suspend fun getPriceConfiguration(): PriceConfigurationEntity
}
