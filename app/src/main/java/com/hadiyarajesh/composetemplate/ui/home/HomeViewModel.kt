package com.hadiyarajesh.composetemplate.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.data.repository.HomeRepository
import com.hadiyarajesh.composetemplate.utility.ImageUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Initial)
    val uiState: StateFlow<HomeScreenUiState> get() = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUiState.Loading

            try {
                /*
                 * The Image can be null: on first launch the database is empty and the
                 * Flow emits null. Once DatabaseInitializer populates the database, the
                 * Flow emits the stored value.
                 */
                homeRepository
                    .loadData()
                    .collect { image: Image? ->
                        image?.let { msg ->
                            _uiState.value = HomeScreenUiState.Success(data = msg)
                        }
                    }
            } catch (e: Exception) {
                _uiState.value = HomeScreenUiState.Error(msg = e.message ?: "Something went wrong")
            }
        }
    }

    fun changeImage(image: Image) {
        viewModelScope.launch {
            val newImage = image.copy(url = ImageUtility.getRandomImageUrl())
            homeRepository.changeImage(newImage)
        }
    }
}
