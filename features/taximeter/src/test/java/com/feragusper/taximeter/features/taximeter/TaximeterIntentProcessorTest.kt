package com.feragusper.taximeter.features.taximeter

import app.cash.turbine.test
import com.feragusper.taximeter.libraries.ride.domain.RideUpdateCoordinator
import com.feragusper.taximeter.libraries.ride.domain.model.FareSummary
import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import com.feragusper.taximeter.libraries.ride.domain.model.Ride
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideFareSummaryUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetCurrentRideUpdatesUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.GetSupplementsUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.StartRideUseCase
import com.feragusper.taximeter.libraries.ride.domain.usecase.UpdateRideSupplementsUseCase
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * Unit tests for the TaximeterIntentProcessor class.
 * This test class validates the processing of various user intents
 * and ensures the correct intent actions are triggered.
 */
class TaximeterIntentProcessorTest {

    private val getSupplementsUseCase: GetSupplementsUseCase = mockk()
    private val startRideUseCase: StartRideUseCase = mockk()
    private val rideUpdateCoordinator: RideUpdateCoordinator = mockk(relaxUnitFun = true)
    private val getCurrentRideUpdatesUseCase: GetCurrentRideUpdatesUseCase = mockk()
    private val updateRideSupplementsUseCase: UpdateRideSupplementsUseCase = mockk()
    private val getCurrentRideFareSummaryUseCase: GetCurrentRideFareSummaryUseCase = mockk()

    private lateinit var subject: TaximeterIntentProcessor

    @BeforeEach
    fun setUp() {
        subject = TaximeterIntentProcessor(
            getSupplementsUseCase,
            startRideUseCase,
            rideUpdateCoordinator,
            getCurrentRideUpdatesUseCase,
            updateRideSupplementsUseCase,
            getCurrentRideFareSummaryUseCase
        )
    }

    @Nested
    inner class OpenIntentTests {
        @Test
        fun `when intent is Open then process correctly`() = runTest {
            // Given
            val luggageSupplementId = UUID.fromString("3dcac3cb-1a27-475a-84ea-85db4a887a95")
            val luggageSupplement = Supplement(luggageSupplementId, "Luggage", 5.0)
            val ride = mockk<Ride> {
                every { totalPrice } returns 20.0
                every { timeInSeconds } returns 300
                every { route } returns listOf(mockk())
                every { supplements } returns listOf(luggageSupplement)
                every { distance } returns 0.0
            }

            coEvery { getCurrentRideUpdatesUseCase() } returns flowOf(Result.success(ride))
            coEvery { getSupplementsUseCase() } returns Result.success(listOf(luggageSupplement))

            // When
            subject(TaximeterIntent.Open).test {
                awaitItem() shouldBe TaximeterIntentAction.UpdateFare(
                    price = 20.0,
                    elapsedTime = 300,
                    supplements = mapOf(luggageSupplement to 1),
                    distance = 0.0
                )
                awaitComplete()
            }
        }

        @Test
        fun `when intent is Open and ride fails then return error`() = runTest {
            // Given
            coEvery { getCurrentRideUpdatesUseCase() } returns flowOf(Result.failure(Throwable("Error")))

            // When
            subject(TaximeterIntent.Open).test {
                awaitItem() shouldBe TaximeterIntentAction.ShowError
                awaitComplete()
            }
        }
    }

    @Nested
    inner class StartRideIntentTests {
        @Test
        fun `when intent is StartRide then start ride successfully`() = runTest {
            // Given
            coEvery { startRideUseCase(any()) } returns Result.success(Unit)

            // When
            subject(TaximeterIntent.StartRide(emptyList())).test {
                awaitItem() shouldBe TaximeterIntentAction.Loading
                awaitItem() shouldBe TaximeterIntentAction.StartRide
                awaitComplete()
            }

            // Verify coordinator starts ride updates
            coVerify { rideUpdateCoordinator.startRideUpdates() }
        }

        @Test
        fun `when intent is StartRide and fails then show error`() = runTest {
            // Given
            coEvery { startRideUseCase(any()) } returns Result.failure(Throwable("Error"))

            // When
            subject(TaximeterIntent.StartRide(emptyList())).test {
                awaitItem() shouldBe TaximeterIntentAction.Loading
                awaitItem() shouldBe TaximeterIntentAction.ShowError
                awaitComplete()
            }
        }
    }

    @Nested
    inner class UpdateSupplementsIntentTests {
        @Test
        fun `when intent is UpdateSupplements then update supplements successfully`() = runTest {
            // Given
            coEvery { updateRideSupplementsUseCase(any()) } returns Result.success(Unit)

            // When
            subject(TaximeterIntent.UpdateSupplements(emptyList())).test {
                awaitComplete()
            }

            coVerify { updateRideSupplementsUseCase(any()) }
        }

        @Test
        fun `when intent is UpdateSupplements and fails then show error`() = runTest {
            // Given
            coEvery { updateRideSupplementsUseCase(any()) } returns Result.failure(Throwable("Error"))

            // When
            subject(TaximeterIntent.UpdateSupplements(emptyList())).test {
                awaitItem() shouldBe TaximeterIntentAction.ShowError
                awaitComplete()
            }
        }
    }

    @Nested
    inner class StopRideIntentTests {
        @Test
        fun `when intent is StopRide then stop ride and show fare summary`() = runTest {
            // Given
            val fareSummary = mockk<FareSummary>()
            coEvery { getCurrentRideFareSummaryUseCase() } returns Result.success(fareSummary)

            // When
            subject(TaximeterIntent.StopRide).test {
                awaitItem() shouldBe TaximeterIntentAction.StopRide
                awaitItem() shouldBe TaximeterIntentAction.UpdateFareSummary(fareSummary)
                awaitComplete()
            }

            coVerify { rideUpdateCoordinator.stopRideUpdates() }
        }

        @Test
        fun `when intent is StopRide and fails to get fare summary then show error`() = runTest {
            // Given
            coEvery { getCurrentRideFareSummaryUseCase() } returns Result.failure(Throwable("Error"))

            // When
            subject(TaximeterIntent.StopRide).test {
                awaitItem() shouldBe TaximeterIntentAction.StopRide
                awaitItem() shouldBe TaximeterIntentAction.ShowError
                awaitComplete()
            }
        }
    }
}
