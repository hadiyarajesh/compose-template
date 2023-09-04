package com.hadiyarajesh.composetemplate.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadiyarajesh.composetemplate.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _response = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Empty)
    val response: StateFlow<HomeScreenUiState> = _response.asStateFlow()

    init {
        Log.i(this::class.simpleName, "${this::class.simpleName} initialized")
    }

    fun loadData() {
        viewModelScope.launch {
            _response.value = HomeScreenUiState.Loading

//            homeRepository.loadData().collect { message ->
//                _response.value = HomeScreenUiState.Success(data = message.text)
//            }
        }
    }
}
