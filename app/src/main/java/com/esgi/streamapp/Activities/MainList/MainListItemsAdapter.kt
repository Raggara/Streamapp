package com.esgi.streamapp.Activities.MainList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_movie_item.view.*

class MainListItemsAdapter(private val movies : List<Movie>, private val context: Context)
    : RecyclerView.Adapter<MainListItemsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val v =  LayoutInflater.from(parent.context).inflate(R.layout.rv_movie_item,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val movie = movies[position]
        Picasso.get().load(movie.image).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_IDMOV, movie.id)
            ContextCompat.startActivity(context, intent, null)
        }
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.rv_movie_img
    }
}