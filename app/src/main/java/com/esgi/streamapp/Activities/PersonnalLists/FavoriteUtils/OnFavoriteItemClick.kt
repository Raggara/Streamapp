package com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils

import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.Movie


interface OnFavoriteItemClick {
    fun onMovieClicked(favorite: Favorites?)
}