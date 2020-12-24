package com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.utils.models.Favorites
import com.esgi.streamapp.utils.models.History


class HistoryAdapter(private val history: List<History?>, private val onMediaClicked: OnHistoryItemClick, private val context: Context) : RecyclerView.Adapter<HistoryViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        return HistoryViewHolder(inflater, parent, this.context)
    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int)
    {
        val history: History = history[position] as History
        holder.bind(history, onMediaClicked)
    }
    override fun getItemCount(): Int = history.size
}