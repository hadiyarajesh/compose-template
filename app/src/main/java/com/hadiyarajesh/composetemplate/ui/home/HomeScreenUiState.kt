package com.hadiyarajesh.composetemplate.ui.home

import com.hadiyarajesh.composetemplate.data.entity.Image

/**
 * Sealed class to represent UI states in [HomeScreen]
 */
internal sealed interface HomeScreenUiState {
    data object Initial : HomeScreenUiState
    data object Loading : HomeScreenUiState
    data class Success(val data: Image) : HomeScreenUiState
    data class Error(val msg: String) : HomeScreenUiState
}
