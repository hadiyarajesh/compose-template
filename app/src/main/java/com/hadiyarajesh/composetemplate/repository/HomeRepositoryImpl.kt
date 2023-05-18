package com.hadiyarajesh.composetemplate.repository

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor() : HomeRepository {
    init {
        Log.i(this::class.simpleName, "${this::class.simpleName} initialized")
    }

    override fun loadData(): String {
        return "Welcome to Compose Template"
    }
}
