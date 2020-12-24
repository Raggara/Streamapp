package com.esgi.streamapp.Activities.PersonnalLists.FavoriteUtils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.History
import com.squareup.picasso.Picasso
import java.lang.Exception


class HistoryViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val context: Context) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.favorite_items, parent, false)), View.OnClickListener
{

    private var image: ImageView? = null
    private var title: TextView? = null
    private var idMovie: Int = -1
    private var history: History? = null
    private var onMediaClicked: OnHistoryItemClick? = null
    private var imgMenu: ImageView? = null

    init
    {
        image = itemView.findViewById(R.id.media_image)
        title = itemView.findViewById(R.id.media_title)
        imgMenu = itemView.findViewById(R.id.imgMenu)
    }

    fun bind(history: History, onMediaClicked: OnHistoryItemClick)
    {
        this.history = history
        this.onMediaClicked = onMediaClicked
        image?.let { Picasso.get().load(history.imagePath).into(it) }
        title?.text = history.title
        idMovie = history.id

        itemView.setOnClickListener(this)
        imgMenu?.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this.context,imgMenu)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.share ->{
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT,
                                """Toi aussi viens regarder ${history.title} sur StreamApp !
                                    |Tu veux l'appli? Envoie "Appli" au 61212.
                                """.trimMargin()
                            )
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(this.context, shareIntent, null)
                    }
                }
                true
            })

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopupMenu = fieldMPopup.get(popupMenu)
                mPopupMenu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopupMenu, true)
            }catch (e: Exception){
                Log.e("Popup", "Erreur")
            }
            popupMenu.show()
        }
    }


    override fun onClick(p0: View?)
    {
        onMediaClicked?.onMovieClicked(history)
    }

}