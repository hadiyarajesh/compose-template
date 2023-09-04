package com.hadiyarajesh.composetemplate.repository

import android.util.Log
import com.hadiyarajesh.composetemplate.data.dao.MessageDao
import com.hadiyarajesh.composetemplate.data.entity.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : HomeRepository {
    init {
        Log.i(this::class.simpleName, "${this::class.simpleName} initialized")
    }

    override fun loadData(): Flow<Message> {
        return messageDao.getMessage()
    }
}
