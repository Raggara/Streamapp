package com.esgi.streamapp.utils.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "VideoPlaying")
data class VideoPlaying(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "path") var path: String,
    @ColumnInfo(name = "position") var position: Long
)

@Entity(tableName = "Preferences")
data class Preferences(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "pip") var pip: Boolean,
    @ColumnInfo(name = "dark_theme") var dark_theme: Boolean
)

@Entity(tableName = "Favorites")
data class Favorites(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "movieId") var movieId: Int,
    @ColumnInfo(name = "imagePath") var imagePath: String,
    @ColumnInfo(name = "title") var title: String
)

@Entity(tableName = "History")
data class History(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "movieId") var movieId: Int,
    @ColumnInfo(name = "imagePath") var imagePath: String,
    @ColumnInfo(name = "title") var title: String
)