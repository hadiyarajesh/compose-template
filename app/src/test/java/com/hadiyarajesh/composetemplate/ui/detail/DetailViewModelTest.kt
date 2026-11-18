package com.hadiyarajesh.composetemplate.ui.detail

import app.cash.turbine.test
import com.hadiyarajesh.composetemplate.data.TestDataGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DetailViewModelTest {
    @BeforeEach
    fun setup() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @AfterEach
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `verify-initial-state-has-no-image`() = runTest {
        DetailViewModel().uiState.test {
            Assertions.assertNull(awaitItem().image)
        }
    }

    @Test
    fun `verify-start-exposes-image`() = runTest {
        val image = TestDataGenerator.getRandomImage()
        val viewModel = DetailViewModel()

        viewModel.uiState.test {
            skipItems(1) // Initial state
            viewModel.start(image)
            Assertions.assertEquals(image, awaitItem().image)
        }
    }

    @Test
    fun `verify-open-url-failure-emits-event`() = runTest {
        val viewModel = DetailViewModel()

        viewModel.openUrlFailedFlow.test {
            viewModel.notifyOpenUrlFailed()
            awaitItem()
        }
    }
}
