package com.hadiyarajesh.composetemplate.repository

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor() {
    init {
        Log.i(this::class.simpleName, "Repository initialized")
    }
}
