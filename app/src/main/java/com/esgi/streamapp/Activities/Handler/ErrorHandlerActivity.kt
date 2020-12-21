package com.esgi.streamapp.Activities.Handler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.esgi.streamapp.Activities.MainList.MainListActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError

class ErrorHandlerActivity : AppCompatActivity() {
    private var errorText : TextView? = null
    private var errorImg  : ImageView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var errorType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.error_layout)
        errorImg = this.findViewById(R.id.errorImg)
        errorText = this.findViewById(R.id.errorText)
        swipeRefreshLayout = this.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        swipeRefreshLayout?.setOnRefreshListener{
            if(Constants.isNetworkAvailable(this)){
                startActivity(Intent(this, MainListActivity::class.java))
            }
            swipeRefreshLayout?.isRefreshing = false
        }

        intent.extras?.get(Constants.EXTRA_ERRTYPE)?.let {
            errorType = it as Int
        }

        Log.d("toto", errorType.toString())
        errorImg?.setImageResource(R.drawable.error)
        errorText?.text = "Une erreur est survenue."
        errorType?.let {
            when(it){
                0 -> {
                    errorText?.text = Constants.ERRMSG_NET
                    errorImg?.setImageResource(R.drawable.wifi)
                }
                1 -> {
                    errorText?.text = Constants.ERRMSG_API
                    errorImg?.setImageResource(R.drawable.error)
                }
                2-> {
                    errorText?.text = Constants.ERRMSG_PLAYER
                    errorImg?.setImageResource(R.drawable.play)
                }
                else -> {
                    errorText?.text = Constants.ERRMSG_API
                    errorImg?.setImageResource(R.drawable.error)
                }
            }
        }
    }
}