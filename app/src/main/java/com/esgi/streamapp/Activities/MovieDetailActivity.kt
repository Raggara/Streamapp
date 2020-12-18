package com.esgi.streamapp.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Constants
import com.esgi.streamapp.utils.models.Movie
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class MovieDetailActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var movieTitle: TextView;
    private lateinit var movieDescription: TextView;
    private lateinit var movieRate: RatingBar;
    private lateinit var btnPlay: ImageButton;
    private lateinit var movieImg: ImageView
    private var idMedia: Int = -1
    private var movie: Movie = Movie(-1, "", "", "", 0.0, "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.media_detail)
        intent.extras?.get("idMedia")?.let {
            idMedia = it as Int
        }
        movieTitle = this.findViewById(R.id.title)
        movieDescription = this.findViewById(R.id.description)
        movieRate = this.findViewById(R.id.rating)
        btnPlay = this.findViewById(R.id.btnPlay)
        movieImg = this.findViewById(R.id.mediaImg)
        btnPlay.setOnClickListener(this)
        initData()
    }

    private fun initData(){
        doAsync {
            val url = URL(Constants.URL_SERV+"/infos?id=$idMedia")
            val stringResponse = url.readText()
            Log.d("res", stringResponse)
            movie = Gson().fromJson(stringResponse, Movie::class.java)
            uiThread {
                Log.d("toto", "movie = $movie")
                movieDescription.text = movie.description
                movieTitle.text = movie.title
                movieRate.rating = movie.rate.toFloat()
                Picasso.get().load(movie.image).resize(movieImg.width, movieImg.height).onlyScaleDown().into(movieImg)
            }
        }

    }

    override fun onClick(v: View?) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("path", movie.path)
        startActivity(intent)
    }


}


