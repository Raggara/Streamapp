package com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.utils.models.Favorites


class FavoriteAdapter(
    private var favorites: List<Favorites?>,
    private val onMediaClicked: OnFavoriteItemClick,
    private val context: Context
) : RecyclerView.Adapter<FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteViewHolder(inflater, parent, this.context)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite: Favorites = favorites[position] as Favorites
        holder.bind(favorite, onMediaClicked)
    }

    fun setData(fav: List<Favorites?>) {
        favorites = fav
    }

    override fun getItemCount(): Int = favorites.size
}