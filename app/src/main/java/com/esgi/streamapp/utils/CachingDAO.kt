package com.esgi.streamapp.utils

import androidx.room.*
import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.History
import com.esgi.streamapp.utils.models.VideoPlaying

@Dao
interface VideoPlayingCachingDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg vid: VideoPlaying)

    @Query("SELECT * FROM VideoPlaying WHERE path LIKE :path")
    fun findByPath(path: String): VideoPlaying

    @Delete
    fun delete(vid: VideoPlaying)

    @Update
    fun update(vararg vid: VideoPlaying)
}

@Dao
interface FavoritesCachingDAO{
    @Query("SELECT * FROM Favorites")
    fun getFav(): List<Favorites>

    @Query("SELECT * FROM Favorites WHERE movieId LIKE :id")
    fun getFavById(id: Int): Favorites

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg fav: Favorites)

    @Delete
    fun delete(fav: Favorites)

    @Update
    fun update(vararg fav: Favorites)
}

@Dao
interface HistoryCachingDAO{
    @Query("SELECT * FROM History")
    fun getHistory(): List<History>

    @Delete
    fun delete(hist: History)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg hist: History)
}