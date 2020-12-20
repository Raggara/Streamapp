package com.esgi.streamapp.Activities.ListMovies

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.Activities.PlayerActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.all_recycler_items.view.*
import java.lang.Exception


class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val context: Context) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.all_recycler_items, parent, false)), View.OnClickListener
{

    private var image: ImageView? = null
    private var title: TextView? = null
    private var idMovie: Int = -1
    private var movie: Movie? = null
    private var onMediaClicked: OnMovieItemClick? = null
    private var imgMenu: ImageView? = null

    init
    {
        image = itemView.findViewById(R.id.media_image)
        title = itemView.findViewById(R.id.media_title)
        imgMenu = itemView.findViewById(R.id.imgMenu)
    }

    fun bind(movie: Movie, onMediaClicked: OnMovieItemClick)
    {
        this.movie = movie
        this.onMediaClicked = onMediaClicked
        image?.let { Picasso.get().load(movie.image).into(it) }
        title?.text = movie.title
        idMovie = movie.id

        itemView?.setOnClickListener(this)
        imgMenu?.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this.context,imgMenu)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.share ->{
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "Hey, tu connais StreamApp? L'appli de streaming pour les BG.")
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(this.context, shareIntent, null)
                    }
                    R.id.play -> {
                        val intent = Intent(this.context, PlayerActivity::class.java)
                        intent.putExtra("path", movie?.path)
                        startActivity(this.context, intent, null)
                    }

                    R.id.movieDetail -> {
                        val intent = Intent(this.context, MovieDetailActivity::class.java)
                        intent.putExtra("idMovie", movie?.id)
                        startActivity(this.context,intent, null)
                    }
                }
                true
            })

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopupMenu = fieldMPopup.get(popupMenu)
                mPopupMenu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopupMenu, true)
            }catch (e: Exception){
                Log.e("Popup", "Erreur")
            }
            popupMenu.show()
        }
    }


    override fun onClick(p0: View?)
    {
        onMediaClicked?.onMovieClicked(movie)
    }

}