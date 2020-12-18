package com.esgi.streamapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.Activities.MainList.MainListActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError
import java.io.Serializable

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.splash_screen, null, false)
        setContentView(view)
        Handler(Looper.getMainLooper()).postDelayed({
            if(Constants.isNetworkAvailable(this)) {
                startActivity(Intent(this, MainListActivity::class.java))
            }else{
                startActivity(Intent(this, ErrorHandlerActivity::class.java))
                val error = ErrorHelper(TypeError.Network, 404, "Vous n'êtes pas connecté à internet.")
                intent.putExtra("error", error)
            }
            finish()
        }, 1500)
    }
}