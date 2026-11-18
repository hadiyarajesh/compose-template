package com.hadiyarajesh.composetemplate.ui.detail

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

internal data class DetailUiState(val image: Image? = null)

internal sealed interface DetailAction {
    data class Start(val image: Image) : DetailAction
}

@HiltViewModel
internal class DetailViewModel @Inject constructor() : ViewModel() {
    private val actions = Channel<DetailAction>(Channel.UNLIMITED)

    // A Channel, not a SharedFlow: it holds the event while nothing collects, e.g. during rotation.
    private val openUrlFailed = Channel<Unit>(Channel.BUFFERED)

    val uiState: StateFlow<DetailUiState> = viewModelScope.launchMolecule(
        mode = RecompositionMode.Immediate
    ) {
        present()
    }

    // One-shot event, kept out of uiState. Meant for a single collector.
    val openUrlFailedFlow: Flow<Unit> = openUrlFailed.receiveAsFlow()

    fun start(image: Image) {
        actions.trySend(DetailAction.Start(image))
    }

    fun notifyOpenUrlFailed() {
        openUrlFailed.trySend(Unit)
    }

    @Composable
    private fun present(): DetailUiState {
        var image by remember { mutableStateOf<Image?>(null) }

        LaunchedEffect(Unit) {
            for (action in actions) {
                when (action) {
                    is DetailAction.Start -> image = action.image
                }
            }
        }

        return DetailUiState(image = image)
    }
}
