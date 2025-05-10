package com.hadiyarajesh.composetemplate.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.dao.ImageDao
import com.hadiyarajesh.composetemplate.data.entity.Image
import com.hadiyarajesh.composetemplate.utility.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * This class implements [RoomDatabase.Callback] and override [RoomDatabase.Callback.onCreate] method
 * to initialise room database when it's created for the first time.
 * We're using [Provider] of [ImageDao] to break the circular dependency.
 */
class RoomDbInitializer(
    private val context: Context,
    private val imageDaoProvider: Provider<ImageDao>
) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        imageDaoProvider.get().insertOrUpdateImage(
            Image(
                description = context.getString(R.string.welcome_message),
                altText = context.getString(R.string.image),
                url = Constants.IMAGE_URL
            )
        )
    }
}
