package com.esgi.streamapp.Activities.ListMovies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.Movie
import com.esgi.streamapp.utils.models.TypeError
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
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
        recycler = this.findViewById(R.id.main_recyclerview);
        pgrBar = this.findViewById(R.id.pgrBar)
        if (!Constants.isNetworkAvailable(this)){
            startActivity(Intent(this, ErrorHandlerActivity::class.java))
            val error = ErrorHelper(TypeError.Network, 404, "Vous n'êtes pas connecté à internet.")
            intent.putExtra("error", error)
            finish()
        }
        intent.extras?.get("category")?.let {
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
                        layoutManager = LinearLayoutManager(this@MovieListActivity)
                        adapter = MovieAdapter(
                            it,
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
        intent.putExtra("idMovie", movie?.id)
        startActivity(intent)
    }
}