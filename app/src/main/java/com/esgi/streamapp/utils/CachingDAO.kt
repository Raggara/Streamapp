package com.esgi.streamapp.utils

import androidx.room.*
import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.Preferences
import com.esgi.streamapp.utils.models.VideoPlaying

@Dao
interface VideoPlayingCachingDAO {
    @Insert
    fun insert(vararg vid: VideoPlaying)

    @Query("SELECT * FROM VideoPlaying WHERE path LIKE :path")
    fun findByPath(path: String): VideoPlaying

    @Delete
    fun delete(vid: VideoPlaying)

    @Update
    fun update(vararg vid: VideoPlaying)
}

@Dao
interface PreferencesCachingDAO{
    @Insert
    fun insert(vararg pref: Preferences)

    @Query("SELECT * FROM Preferences")
    fun getPref(): List<Preferences>

    @Delete
    fun delete(pref: Preferences)

    @Update
    fun update(vararg pref: Preferences)
}

@Dao
interface FavoritesCachingDAO{
    @Query("SELECT * FROM Favorites")
    fun getFav(): List<Favorites>

    @Query("SELECT * FROM Favorites WHERE movieId LIKE :id")
    fun getFavById(id: Int): Favorites

    @Insert
    fun insert(vararg fav: Favorites)

    @Delete
    fun delete(fav: Favorites)

    @Update
    fun update(vararg fav: Favorites)
}