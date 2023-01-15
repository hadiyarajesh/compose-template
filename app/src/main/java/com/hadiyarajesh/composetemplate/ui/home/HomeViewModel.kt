package com.hadiyarajesh.composetemplate.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hadiyarajesh.composetemplate.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    init {
        Log.i(this::class.simpleName, "${this::class.simpleName} initialized")
    }
}
