package com.esgi.streamapp.Activities.ListMovies

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Movie
import com.squareup.picasso.Picasso

class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.all_recycler_items, parent, false)), View.OnClickListener
{

    private var image: ImageView? = null
    private var title: TextView? = null
    private var idMovie: Int = -1
    private var movie: Movie? = null
    private var onMediaClicked: OnMovieItemClick? = null

    init
    {
        image = itemView.findViewById(R.id.media_image)
        title = itemView.findViewById(R.id.media_title)


    }

    fun bind(movie: Movie, onMediaClicked: OnMovieItemClick)
    {
        this.movie = movie
        this.onMediaClicked = onMediaClicked
        image?.let { Picasso.get().load(movie.image).into(it) }
        title?.text = movie.title
        idMovie = movie.id

        itemView?.setOnClickListener(this)
    }

    override fun onClick(p0: View?)
    {
        onMediaClicked?.onMovieClicked(movie)
    }

}