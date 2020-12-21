package com.esgi.streamapp.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.Movie
import com.esgi.streamapp.utils.models.TypeError
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.Serializable
import java.net.URL

class MovieDetailActivity: AppCompatActivity(), View.OnClickListener {

    private var movieTitle: TextView? = null;
    private var movieDescription: TextView? = null;
    private var movieRate: RatingBar? = null;
    private var btnPlay: ImageButton? = null;
    private var movieImg: ImageView? = null
    private var idMedia: Int = -1
    private var movie: Movie = Movie(-1, "", "", "", 0.0, "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.media_detail)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        if (!Constants.isNetworkAvailable(this)){
            intent.putExtra(Constants.EXTRA_ERRTYPE, 0)
            startActivity(Intent(this, ErrorHandlerActivity::class.java))
            finish()
        }
        intent.extras?.get(Constants.EXTRA_IDMOV)?.let {
            idMedia = it as Int
        }
        movieTitle = this.findViewById(R.id.title)
        movieDescription = this.findViewById(R.id.description)
        movieRate = this.findViewById(R.id.rating)
        btnPlay = this.findViewById(R.id.btnPlay)
        movieImg = this.findViewById(R.id.mediaImg)
        btnPlay?.setOnClickListener(this)
        initData()
    }

    private fun initData(){
        doAsync {
            val url = URL(Constants.URL_SERV+"/infos?id=$idMedia")
            val stringResponse = url.readText()
            movie = Gson().fromJson(stringResponse, Movie::class.java)
            uiThread {
                movieDescription?.text = movie.description
                movieTitle?.text = movie.title
                movieRate?.rating = movie.rate.toFloat()
                movieImg?.let { Picasso.get().load(movie.image).resize(it.width, it.height).onlyScaleDown().into(it) }
            }
        }
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(Constants.EXTRA_PATHMOV, movie.path)
        startActivity(intent)
    }


}


