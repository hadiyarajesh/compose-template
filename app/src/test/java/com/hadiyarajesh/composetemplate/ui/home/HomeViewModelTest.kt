package com.hadiyarajesh.composetemplate.ui.home

import app.cash.turbine.test
import com.hadiyarajesh.composetemplate.data.TestDataGenerator
import com.hadiyarajesh.composetemplate.data.repository.TestHomeRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HomeViewModelTest {
    private lateinit var homeRepository: TestHomeRepository

    @BeforeEach
    fun setup() {
        homeRepository = TestHomeRepository()
    }

    @Test
    fun `verify-initial-state`() {
        runTest {
            val viewModel = newViewModel()
            viewModel.uiState.test {
                Assertions.assertTrue(awaitItem() is HomeScreenUiState.Initial)
            }
        }
    }

    @Test
    fun `verify-success-state`() {
        val randomImage = TestDataGenerator.getRandomImage()

        homeRepository.imagesToEmit = buildList {
            add(randomImage)
        }

        runTest {
            val viewModel = newViewModel()
            viewModel.uiState.test {
                viewModel.loadData()

                skipItems(1) // Initial state
                Assertions.assertTrue(awaitItem() is HomeScreenUiState.Loading)

                val successState = awaitItem()
                Assertions.assertTrue(successState is HomeScreenUiState.Success)

                val retrievedImage = (successState as HomeScreenUiState.Success).data
                Assertions.assertEquals(randomImage, retrievedImage)
            }
        }
    }

    @Test
    fun `verify-error-state`() {
        homeRepository.throwError = true

        runTest {
            val viewModel = newViewModel()
            viewModel.uiState.test {
                viewModel.loadData()

                skipItems(1) // Initial state
                Assertions.assertTrue(awaitItem() is HomeScreenUiState.Loading)
                val errorState = awaitItem()
                Assertions.assertTrue(errorState is HomeScreenUiState.Error)
            }
        }
    }

    private fun newViewModel(): HomeViewModel {
        return HomeViewModel(homeRepository)
    }
}
