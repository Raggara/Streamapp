package com.esgi.streamapp.Activities.Handler

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError

class ErrorHandlerActivity : AppCompatActivity() {
    private var errorText : TextView? = null
    private var errorImg  : ImageView? = null
    private var error: ErrorHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.error_layout)
        errorImg = this.findViewById(R.id.errorImg)
        errorText = this.findViewById(R.id.errorText)

        intent.extras?.getSerializable("error")?.let {
           error  = it as ErrorHelper
           Log.d("erreur", it.toString())
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