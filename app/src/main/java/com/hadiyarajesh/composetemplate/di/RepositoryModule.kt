package com.hadiyarajesh.composetemplate.di

import com.hadiyarajesh.composetemplate.data.repository.HomeRepository
import com.hadiyarajesh.composetemplate.data.repository.HomeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Dagger-Hilt module that provides repository bindings scoped to ViewModel lifecycle.
 *
 * This module includes:
 * - A binding from [HomeRepositoryImpl] to [HomeRepository] using [@Binds].
 *
 * Annotated with [@InstallIn(ViewModelComponent::class)] to ensure the bound instance
 * is scoped to the ViewModel's lifecycle.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindHomeRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository
}
