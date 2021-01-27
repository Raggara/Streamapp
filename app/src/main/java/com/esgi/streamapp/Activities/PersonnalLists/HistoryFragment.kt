package com.esgi.streamapp.Activities.PersonnalLists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.ListMovies.MovieAdapter
import com.esgi.streamapp.Activities.ListMovies.OnMovieItemClick
import com.esgi.streamapp.Activities.MovieDetailActivity
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.HistoryAdapter
import com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils.OnHistoryItemClick
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.AppDatabase
import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.History
import com.esgi.streamapp.utils.models.Movie
import kotlinx.android.synthetic.main.fragment_favourites.view.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class HistoryFragment : Fragment(), OnHistoryItemClick {
    private var history: List<History>? = null
    private var recycler: RecyclerView? = null
    private var pgrBar: ProgressBar? = null
    private var db : AppDatabase? = null
    private var mContext: Context? = null
    private var refresh: Boolean = false
    private var imgNoHisto: ImageView? = null
    private var textNoHisto: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_history, container, false)
        pgrBar = view.findViewById(R.id.pgrBar)
        recycler = view.hist_recyclerview
        imgNoHisto = view.findViewById(R.id.img_noHisto)
        textNoHisto = view.findViewById(R.id.text_nohisto)
        return view
    }

    override fun onStart() {
        super.onStart()
        initData()
    }

    private fun initData(){
        doAsync {
            history = db?.historyDAO()?.getHistory()

            uiThread {
                history?.let {
                    recycler?.apply {
                        layoutManager = GridLayoutManager((activity as PersonnalListsActivity).getContext(), 2)
                        adapter = HistoryAdapter(it,this@HistoryFragment, context)
                    }
                    if(it.isEmpty()){
                        recycler?.visibility = View.GONE
                        imgNoHisto?.visibility = View.VISIBLE
                        textNoHisto?.visibility = View.VISIBLE
                        pgrBar?.visibility = View.GONE
                    }else{
                        recycler?.visibility = View.VISIBLE
                        imgNoHisto?.visibility = View.GONE
                        textNoHisto?.visibility = View.GONE
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

    override fun onMovieClicked(history: History?) {
        val intent = Intent(mContext, MovieDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_IDMOV, history?.movieId)
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