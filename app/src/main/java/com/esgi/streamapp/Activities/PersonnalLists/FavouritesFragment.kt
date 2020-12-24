package com.esgi.streamapp.Activities.PersonnalLists

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.FavoriteAdapter
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.HistoryAdapter
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.OnFavoriteItemClick
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.OnHistoryItemClick
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.AppDatabase
import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.History
import kotlinx.android.synthetic.main.fragment_favourites.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class FavouritesFragment : Fragment(), OnFavoriteItemClick {
    private var favorites: List<Favorites>? = null
    private var recycler: RecyclerView? = null
    private var pgrBar: ProgressBar? = null
    private var db : AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_favourites, container, false)
        pgrBar = view.findViewById(R.id.pgrBar)
        recycler = view.fav_recyclerview

        return view
    }

    private fun initData(){
        doAsync {
            favorites = db?.favoritesDAO()?.getFav()

            uiThread {
                favorites?.let {
                    recycler?.apply {
                        layoutManager = GridLayoutManager((activity as PersonnalListsActivity).getContext(), 2)
                        adapter = FavoriteAdapter(it,this@FavouritesFragment, context)
                    }
                    pgrBar?.visibility = View.GONE
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = AppDatabase(context)
    }

    override fun onMovieClicked(favorite: Favorites?) {
        Log.d("clické", "clické")
    }
}