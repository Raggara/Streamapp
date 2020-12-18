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
import com.esgi.streamapp.utils.models.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError
import java.io.Serializable

class ErrorHandlerActivity : AppCompatActivity() {
    private var errorText : TextView? = null
    private var errorImg  : ImageView? = null
    private var error: ErrorHelper? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

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


        intent.extras?.getSerializable("error")?.let {
           error  = it as ErrorHelper
        }

        errorImg?.setImageResource(R.drawable.error)
        errorText?.text = "Une erreur est survenue."
        error?.let {
            when(it.errorType){
                TypeError.Network -> errorImg?.setImageResource(R.drawable.wifi)
                TypeError.API -> errorImg?.setImageResource(R.drawable.error)
                TypeError.Player-> errorImg?.setImageResource(R.drawable.play)
                else -> errorImg?.setImageResource(R.drawable.error)
            }
            errorText?.text = error?.errorMessage
        }
    }
}