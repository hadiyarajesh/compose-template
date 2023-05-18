package com.hadiyarajesh.composetemplate.ui.home

sealed interface HomeScreenUiState {
    object Empty : HomeScreenUiState
    object Loading : HomeScreenUiState
    data class Success(val data: String) : HomeScreenUiState
    data class Error(val msg: String) : HomeScreenUiState
}
