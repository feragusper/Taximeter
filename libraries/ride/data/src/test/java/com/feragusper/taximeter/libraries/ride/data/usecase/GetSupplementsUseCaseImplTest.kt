package com.feragusper.taximeter.libraries.ride.data.usecase

import com.feragusper.taximeter.libraries.ride.domain.model.Supplement
import com.feragusper.taximeter.libraries.ride.domain.repository.SupplementRepository
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetSupplementsUseCaseImplTest {
    private val supplementRepository: SupplementRepository = mockk()

    private lateinit var subject: GetSupplementsUseCaseImpl

    @BeforeEach
    fun setup() {
        subject = GetSupplementsUseCaseImpl(supplementRepository)
    }

    @Test
    fun `get supplements success`() =
        runTest {
            // Given
            val supplements: List<Supplement> = mockk()
            every { supplementRepository.getSupplements() } returns supplements

            // When
            val result = subject.invoke()

            // Then
            result shouldBeSuccess supplements
        }

    @Test
    fun `get supplements failure`() =
        runTest {
            // Given
            val exception = Exception()
            every { supplementRepository.getSupplements() } throws exception

            // When
            val result = subject.invoke()

            // Then
            result shouldBeFailure exception
        }
}
