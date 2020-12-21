package com.esgi.streamapp.Activities

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.Activities.MainList.MainListActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError
import java.io.Serializable


class SplashActivity : AppCompatActivity() {

    private var vidSplash :  VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.splash_screen, null, false)
        setContentView(view)
        this.vidSplash = findViewById(R.id.vidSplash)

        val video: Uri = Uri.parse("android.resource://$packageName/raw/splash")
        vidSplash?.setVideoURI(video)

        vidSplash?.start()
        Handler(Looper.getMainLooper()).postDelayed({
            if(Constants.isNetworkAvailable(this)) {
                startActivity(Intent(this, MainListActivity::class.java))
            }else{
                intent.putExtra(Constants.EXTRA_ERRTYPE, 0)
                startActivity(Intent(this, ErrorHandlerActivity::class.java))
                finish()
            }
            finish()
        }, 3000)
    }
}