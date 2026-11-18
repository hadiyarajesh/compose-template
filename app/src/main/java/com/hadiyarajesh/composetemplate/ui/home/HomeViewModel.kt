package com.hadiyarajesh.composetemplate.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.data.repository.HomeRepository
import com.hadiyarajesh.composetemplate.utility.ImageUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    // Public functions only send actions. present() is the single place state changes.
    private val actions = Channel<HomeAction>(Channel.UNLIMITED)

    // Molecule runs present() in viewModelScope and exposes the result as a StateFlow,
    // so there is no MutableStateFlow to keep in sync.
    val uiState: StateFlow<HomeScreenUiState> = viewModelScope.launchMolecule(
        mode = RecompositionMode.Immediate
    ) {
        present()
    }

    fun start() {
        actions.trySend(HomeAction.Start)
    }

    fun changeImage(image: Image) {
        actions.trySend(HomeAction.ChangeImage(image))
    }

    @Composable
    private fun present(): HomeScreenUiState {
        var state: HomeScreenUiState by remember { mutableStateOf(HomeScreenUiState.Initial) }

        LaunchedEffect(Unit) {
            for (action in actions) {
                when (action) {
                    HomeAction.Start -> if (state is HomeScreenUiState.Initial) {
                        state = HomeScreenUiState.Loading
                        // Own coroutine, so this loop keeps handling other actions.
                        launch {
                            try {
                                /*
                                 * The Image can be null: on first launch the database is empty and the
                                 * Flow emits null. Once DatabaseInitializer populates the database, the
                                 * Flow emits the stored value.
                                 */
                                homeRepository.loadData().collect { image: Image? ->
                                    image?.let { state = HomeScreenUiState.Success(data = it) }
                                }
                            } catch (e: Exception) {
                                state = HomeScreenUiState.Error(msg = e.message ?: "Something went wrong")
                            }
                        }
                    }

                    is HomeAction.ChangeImage -> launch {
                        homeRepository.changeImage(action.image.copy(url = ImageUtility.getRandomImageUrl()))
                    }
                }
            }
        }

        return state
    }
}
