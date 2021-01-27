package com.esgi.streamapp.Activities.MainList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.Activities.ListMovies.MovieListActivity
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.Activities.PersonnalLists.PersonnalListsActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.Serializable
import java.net.URL

class MainListActivity : AppCompatActivity() {
    private var recycler: RecyclerView? = null
    private var pgrBar: ProgressBar? = null
    private var data: MutableList<Category> = mutableListOf()
    private var bottomNav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.home_list_activity)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        if (!Constants.isNetworkAvailable(this)) {
            val intent = Intent(this, ErrorHandlerActivity::class.java)
            intent.putExtra(Constants.EXTRA_ERRTYPE, 0)
            startActivity(intent)
        }
        recycler = this.findViewById(R.id.home_rv);
        pgrBar = this.findViewById(R.id.pgrBar)
        bottomNav = this.findViewById(R.id.navBar)

        bottomNav?.selectedItemId = R.id.home
        bottomNav?.setOnNavigationItemSelectedListener {item ->
            when (item.itemId) {
                R.id.lists -> {
                    startActivity(Intent(this, PersonnalListsActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.navigate -> {
                    Toast.makeText(this, "navigate", Toast.LENGTH_SHORT)
                }
                R.id.preferences -> {
                    Toast.makeText(this, "pref", Toast.LENGTH_SHORT)
                }
            }
            true
        }
        initData()
    }

    private fun initData() {
        doAsync {
            val url = URL(Constants.URL_SERV + "/categories")
            val stringResponse = url.readText()
            val categories: List<Categories> =
                Gson().fromJson(stringResponse, Array<Categories>::class.java).toList()
            categories.forEach {
                val url = URL(Constants.URL_SERV + "/list?type=${it.type}&range=7&page=1")
                val resp: String = url.readText()
                val items: List<Movie> = Gson().fromJson(resp, Array<Movie>::class.java).toList()
                val element = Category(it.type, items);
                data?.add(element)
            }
            uiThread {
                data?.let {
                    recycler?.apply {
                        layoutManager = LinearLayoutManager(this@MainListActivity)
                        adapter = MainListAdapter(
                            it,
                            this@MainListActivity
                        )
                    }
                    pgrBar?.visibility = View.GONE
                }
            }
        }

    }

}