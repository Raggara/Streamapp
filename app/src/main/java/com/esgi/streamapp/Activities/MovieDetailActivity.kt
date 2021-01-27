package com.esgi.streamapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.AppDatabase
import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.History
import com.esgi.streamapp.utils.models.Movie
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class MovieDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var movieTitle: TextView? = null;
    private var movieDescription: TextView? = null;
    private var movieRate: RatingBar? = null;
    private var btnPlay: ImageButton? = null;
    private var movieImg: ImageView? = null
    private var imgFavEmpty: ImageView? = null
    private var imgFavPlain: ImageView? = null
    private var isFavorite: Boolean = false
    private var idMedia: Int = -1
    private var favorites: Favorites? = null
    private var movie: Movie = Movie(-1, "", "", "", 0.0, "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.media_detail)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        if (!Constants.isNetworkAvailable(this)) {
            val intent = Intent(this, ErrorHandlerActivity::class.java)
            intent.putExtra(Constants.EXTRA_ERRTYPE, 0)
            startActivity(intent)
        }
        intent.extras?.get(Constants.EXTRA_IDMOV)?.let {
            idMedia = it as Int
        }
        movieTitle = this.findViewById(R.id.title)
        movieDescription = this.findViewById(R.id.description)
        movieRate = this.findViewById(R.id.rating)
        btnPlay = this.findViewById(R.id.btnPlay)
        movieImg = this.findViewById(R.id.mediaImg)
        imgFavEmpty = this.findViewById(R.id.imgFav)
        imgFavPlain = this.findViewById(R.id.imgFavPlain)
        btnPlay?.setOnClickListener(this)
        initData()
        initFav()
    }

    private fun initFav() {
        this.imgFavPlain?.setOnClickListener {
            doAsync {
                val db = AppDatabase(this@MovieDetailActivity)
                favorites?.let { it -> db.favoritesDAO().delete(it) }
            }
            imgFavPlain?.visibility = View.GONE
            imgFavEmpty?.visibility = View.VISIBLE
        }

        this.imgFavEmpty?.setOnClickListener {
            doAsync {
                val db = AppDatabase(this@MovieDetailActivity)
                db.favoritesDAO().insert(Favorites(movie?.id, movie?.id, movie?.image, movie.title))
            }
            imgFavPlain?.visibility = View.VISIBLE
            imgFavEmpty?.visibility = View.GONE
        }
    }

    private fun initData() {
        doAsync {
            val url = URL(Constants.URL_SERV + "/infos?id=$idMedia")
            val stringResponse = url.readText()
            movie = Gson().fromJson(stringResponse, Movie::class.java)
            val db = AppDatabase(this@MovieDetailActivity)

            favorites = db.favoritesDAO().getFavById(idMedia)
            if (favorites != null) isFavorite = true

            uiThread {
                movieDescription?.text = movie.description
                movieTitle?.text = movie.title
                movieRate?.rating = movie.rate.toFloat()
                movieImg?.let {
                    Picasso.get().load(movie.image).resize(it.width, it.height).onlyScaleDown()
                        .into(it)
                }
                if (isFavorite) {
                    imgFavEmpty?.visibility = View.GONE
                } else {
                    imgFavPlain?.visibility = View.GONE
                }
            }
        }
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(Constants.EXTRA_PATHMOV, movie.path)
        intent.putExtra(Constants.EXTRA_IDMOV, movie.id)
        startActivity(intent)

        doAsync {
            val db = AppDatabase(this@MovieDetailActivity)
            db?.historyDAO().insert(History(movie.id, movie.id, movie.image, movie.title))
        }
    }


}


