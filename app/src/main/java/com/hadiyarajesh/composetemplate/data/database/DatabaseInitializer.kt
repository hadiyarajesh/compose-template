package com.hadiyarajesh.composetemplate.data.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hadiyarajesh.composetemplate.R
import com.hadiyarajesh.composetemplate.data.database.dao.ImageDao
import com.hadiyarajesh.composetemplate.data.database.entity.Image
import com.hadiyarajesh.composetemplate.utility.ImageUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

/**
 * A custom [RoomDatabase.Callback] used to initialize the Room database
 * when it's created for the first time.
 *
 * This implementation overrides [RoomDatabase.Callback.onCreate] to pre-populate
 * the database with initial data.
 *
 * A [Provider] of [ImageDao] is used to break the circular dependency between
 * the database and its DAO.
 */
class DatabaseInitializer(
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
                url = ImageUtility.getRandomImageUrl(),
                description = context.getString(R.string.welcome_message),
                altText = context.getString(R.string.failed_to_load_image)
            )
        )
    }
}
