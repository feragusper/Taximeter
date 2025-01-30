package com.feragusper.taximeter.libraries.ride.data.repository

import com.feragusper.taximeter.libraries.ride.data.datasource.SupplementDataSource
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class SupplementRepositoryImplTest {

    private lateinit var supplementRepository: SupplementRepositoryImpl
    private val supplementDataSource: SupplementDataSource = mockk()

    // Sample data for testing
    private val supplementList = listOf(
        Supplement(UUID.randomUUID(), "Baby Seat", 5.0),
        Supplement(UUID.randomUUID(), "Extra Baggage", 10.0),
        Supplement(UUID.randomUUID(), "Airport Transfer", 25.0)
    )

    @BeforeEach
    fun setUp() {
        supplementRepository = SupplementRepositoryImpl(supplementDataSource)
    }

    @Test
    fun `getSupplements should return the correct list of supplements`() {
        // Given
        every { supplementDataSource.getSupplements() } returns supplementList

        // When
        val result = supplementRepository.getSupplements()

        // Then
        result shouldBe supplementList
    }
}
