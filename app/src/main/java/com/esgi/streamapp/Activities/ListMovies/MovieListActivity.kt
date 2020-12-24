package com.esgi.streamapp.Activities.ListMovies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.*
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.Serializable
import java.net.URL

class MovieListActivity: AppCompatActivity(),
    OnMovieItemClick {
    private var movies: List<Movie>? = null
    private var recycler: RecyclerView? = null
    private var pgrBar: ProgressBar? = null
    private var categoryName: String? = null
    private var titleCat: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.media_list)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        recycler = this.findViewById(R.id.main_recyclerview);
        pgrBar = this.findViewById(R.id.pgrBar)
        if (!Constants.isNetworkAvailable(this)){
            intent.putExtra(Constants.EXTRA_ERRTYPE, 0)
            startActivity(Intent(this, ErrorHandlerActivity::class.java))
            finish()
        }
        intent.extras?.get(Constants.EXTRA_NAMECATEG)?.let {
            categoryName = it as String
        }
        titleCat = this.findViewById(R.id.category_title)
        titleCat?.text = categoryName

        initData()
    }

    private fun initData(){
        doAsync {
            val url = URL(Constants.URL_SERV + "/list?type=$categoryName")
            val stringResponse = url.readText()
            movies = Gson().fromJson(stringResponse , Array<Movie>::class.java).toList()
            uiThread {
                //On attend de recevoir les données pour créer le recyclerview
                movies?.let {
                    recycler?.apply {
                        layoutManager = GridLayoutManager(this@MovieListActivity, 2)
                        adapter = MovieAdapter(
                            it,
                            this@MovieListActivity,
                            this@MovieListActivity
                        )
                    }
                    pgrBar?.visibility = View.GONE
                }
            }
        }
    }

    override fun onMovieClicked(movie: Movie?)
    {
        //Navigation vers la fiche d'info du film
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_IDMOV, movie?.id)
        startActivity(intent)
    }
}