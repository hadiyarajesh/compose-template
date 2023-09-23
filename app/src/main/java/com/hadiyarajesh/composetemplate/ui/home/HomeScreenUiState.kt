package com.hadiyarajesh.composetemplate.ui.home

import com.hadiyarajesh.composetemplate.data.entity.Message

sealed interface HomeScreenUiState {
    data object Initial : HomeScreenUiState
    data object Loading : HomeScreenUiState
    data class Success(val msg: Message) : HomeScreenUiState
    data class Error(val msg: String) : HomeScreenUiState
}
