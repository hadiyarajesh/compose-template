package com.hadiyarajesh.composetemplate.ui.home

import com.hadiyarajesh.composetemplate.data.database.entity.Image

/**
 * Represents all possible UI states for the [HomeScreenContent].
 *
 * This sealed interface helps the UI layer react to state changes
 * in a type-safe and declarative manner.
 */
internal sealed interface HomeScreenUiState {
    data object Initial : HomeScreenUiState

    data object Loading : HomeScreenUiState

    data class Success(val data: Image) : HomeScreenUiState

    data class Error(val msg: String) : HomeScreenUiState
}
