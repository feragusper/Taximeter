package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.repository.RideRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class UpdateRideSupplementsUseCaseImplTest {
    private val rideRepository: RideRepository = mockk()

    private lateinit var subject: UpdateRideSupplementsUseCaseImpl

    @BeforeEach
    fun setup() {
        subject = UpdateRideSupplementsUseCaseImpl(rideRepository)
    }

    @Test
    fun `start ride success`() =
        runTest {
            // Given
            val supplements: List<UUID> = mockk()

            every { rideRepository.updateRideSupplements(supplements) } just Runs

            // When
            val result = subject.invoke(supplements)

            // Then
            result shouldBeSuccess Unit
        }

    @Test
    fun `start ride failure`() =
        runTest {
            // Given
            val exception = Exception()
            val supplements: List<UUID> = mockk()

            every { rideRepository.updateRideSupplements(supplements) } throws exception

            // When
            val result = subject.invoke(supplements)

            // Then
            result shouldBeFailure exception
        }
}
