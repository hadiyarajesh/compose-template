package com.hadiyarajesh.composetemplate.ui.home

import app.cash.turbine.test
import com.hadiyarajesh.composetemplate.data.TestDataGenerator
import com.hadiyarajesh.composetemplate.data.repository.TestHomeRepository
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
internal class HomeViewModelTest {
    private lateinit var homeRepository: TestHomeRepository

    @BeforeEach
    fun setup() {
        // Molecule runs presenters in viewModelScope, which uses Dispatchers.Main.
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeRepository = TestHomeRepository()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
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
                viewModel.start()

                skipItems(1) // Initial state
                // StateFlow conflates Loading when the repository emits immediately.
                val successState = expectMostRecentItem()
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
                viewModel.start()

                skipItems(1) // Initial state
                val errorState = expectMostRecentItem()
                Assertions.assertTrue(errorState is HomeScreenUiState.Error)
            }
        }
    }

    private fun newViewModel(): HomeViewModel = HomeViewModel(homeRepository)
}
