package com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils

import com.esgi.streamapp.utils.models.History


interface OnHistoryItemClick {
    fun onMovieClicked(history: History?)
}