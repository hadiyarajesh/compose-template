package com.hadiyarajesh.composetemplate.repository

import com.hadiyarajesh.composetemplate.data.entity.Message
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun loadData(): Flow<Message>
}
