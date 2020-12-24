package com.esgi.streamapp.utils.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.esgi.streamapp.utils.FavoritesCachingDAO
import com.esgi.streamapp.utils.HistoryCachingDAO
import com.esgi.streamapp.utils.PreferencesCachingDAO
import com.esgi.streamapp.utils.VideoPlayingCachingDAO

@Database(
    entities = [Preferences::class, VideoPlaying::class, Favorites::class, History::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun preferencesDAO(): PreferencesCachingDAO
    abstract fun videoPlayingDAO(): VideoPlayingCachingDAO
    abstract fun favoritesDAO(): FavoritesCachingDAO
    abstract fun historyDAO(): HistoryCachingDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "streamApp.db")
            .build()
    }
}