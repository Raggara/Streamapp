package com.esgi.streamapp.Activities.MainList

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.Activities.ListMovies.MovieListActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.Category
import kotlinx.android.synthetic.main.category_rv.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainListAdapter(private val categories : List<Category>, private val context: Context) :RecyclerView.Adapter<MainListAdapter.ViewHolder>(){
    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.category_rv,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        val category = categories[position]
        holder.textView.text = category.name
        holder.viewAllButton.onClick {
            val intent = Intent(context, MovieListActivity::class.java)
            intent.putExtra(Constants.EXTRA_NAMECATEG, category.name)
            ContextCompat.startActivity(context, intent, null)
        }
        val childLayoutManager = LinearLayoutManager(
            holder.recyclerView.context,
            RecyclerView.HORIZONTAL,
            false
        )
        childLayoutManager.initialPrefetchItemCount = 4
        holder.recyclerView.apply {
            layoutManager = childLayoutManager
            adapter = MainListItemsAdapter(category.movies, context)
            setRecycledViewPool(viewPool)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyclerView : RecyclerView = itemView.rv_list_movies
        val textView:TextView = itemView.rv_category_name
        val viewAllButton : Button = itemView.seeAll
    }

}