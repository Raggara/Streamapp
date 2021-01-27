package com.esgi.streamapp.Activities.PersonnalLists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.FavoriteAdapter
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.OnFavoriteItemClick
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.AppDatabase
import com.esgi.streamapp.utils.models.Favorites
import kotlinx.android.synthetic.main.fragment_favourites.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class FavouritesFragment : Fragment(), OnFavoriteItemClick {
    private var favorites: List<Favorites>? = null
    private var recycler: RecyclerView? = null
    private var pgrBar: ProgressBar? = null
    private var db : AppDatabase? = null
    private var imgNofav: ImageView? = null
    private var textNoFav: TextView? = null
    private var mContext: Context? = null
    private var refresh: Boolean = false

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
        imgNofav = view.findViewById(R.id.img_nofav)
        textNoFav = view.findViewById(R.id.text_nofav)
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
                    if(it.isEmpty()){
                        recycler?.visibility = View.GONE
                        imgNofav?.visibility = View.VISIBLE
                        textNoFav?.visibility = View.VISIBLE
                        pgrBar?.visibility = View.GONE
                    }else{
                        recycler?.visibility = View.VISIBLE
                        imgNofav?.visibility = View.GONE
                        textNoFav?.visibility = View.GONE
                        pgrBar?.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = AppDatabase(context)
        mContext = context
    }

    override fun onMovieClicked(favorite: Favorites?) {
        val intent = Intent(mContext, MovieDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_IDMOV, favorite?.movieId)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if(refresh){
            initData()
        }
    }
    override fun onStop() {
        super.onStop()
        refresh = true
    }
}