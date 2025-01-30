package com.feragusper.taximeter.libraries.ride.data.repository

import com.feragusper.taximeter.libraries.ride.data.datasource.PriceDataSource
import com.feragusper.taximeter.libraries.ride.data.datasource.entity.PriceConfigurationEntity
import com.feragusper.taximeter.libraries.ride.domain.model.PriceConfiguration
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PriceRepositoryImplTest {

    private lateinit var priceRepository: PriceRepositoryImpl
    private val priceDataSource: PriceDataSource = mockk()

    // Sample data for testing
    private val priceConfigurationEntity = PriceConfigurationEntity(
        pricePerKm = 1.5,
        pricePerSecond = 0.25
    )

    private val expectedPriceConfiguration = PriceConfiguration(
        pricePerKm = 1.5,
        pricePerSecond = 0.25
    )

    @BeforeEach
    fun setUp() {
        priceRepository = PriceRepositoryImpl(priceDataSource)
    }

    @Test
    fun `getPriceConfiguration should return correct PriceConfiguration`() = runTest {
        // Given
        coEvery { priceDataSource.getPriceConfiguration() } returns priceConfigurationEntity

        // When
        val result = priceRepository.getPriceConfiguration()

        // Then
        result shouldBe expectedPriceConfiguration
    }
}
