package com.esgi.streamapp.Activities.ListMovies


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Movie

class MovieAdapter(private val movies: List<Movie?>, private val onMediaClicked: OnMovieItemClick, private val context: Context) : RecyclerView.Adapter<MovieViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent, this.context)
    }
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int)
    {
        val movie: Movie = movies[position] as Movie
        holder.bind(movie, onMediaClicked)
    }
    override fun getItemCount(): Int = movies.size
}