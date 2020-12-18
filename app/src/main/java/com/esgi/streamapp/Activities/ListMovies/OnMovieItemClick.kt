package com.esgi.streamapp.Activities.ListMovies

import com.esgi.streamapp.utils.models.Movie


interface OnMovieItemClick {
    fun onMovieClicked(movie: Movie?)
}