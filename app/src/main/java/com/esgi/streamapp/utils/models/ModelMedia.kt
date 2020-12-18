package com.esgi.streamapp.utils.models

data class Movie(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val rate: Double,
    val path: String,
    val type:String
)

data class Categories(val type: String)
data class Category(val name: String, val movies: List<Movie>)