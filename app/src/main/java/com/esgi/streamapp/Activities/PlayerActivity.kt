package com.esgi.streamapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import java.io.Serializable


class PlayerActivity : AppCompatActivity() {
    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private var loading : ProgressBar? = null
    private var moviePath: String? = null
    private var btn_forward: ImageView? = null
    private var btn_backward: ImageView? = null
    private var player_view: PlayerView? = null
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var isFullscreen = false
    private var isPlayerPlaying = true
    private lateinit var mediaItem: MediaItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        if (!Constants.isNetworkAvailable(this)){
            runError(0)
        }
        intent.extras?.get(Constants.EXTRA_PATHMOV)?.let {
            moviePath = it as String
        }

        this.btn_backward = findViewById(R.id.backward)
        this.btn_forward = findViewById(R.id.forward)
        this.loading = findViewById(R.id.loading)

        this.player_view = findViewById(R.id.video_view)
        dataSourceFactory = DefaultDataSourceFactory(this,
            Util.getUserAgent(this, "StreamApp"))

        if (savedInstanceState != null) {
            currentWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            playbackPosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
            isPlayerPlaying = savedInstanceState.getBoolean(STATE_PLAYER_PLAYING)
        }

    }

    private fun initPlayer(){
        val hlsUrl = Constants.URL_STREAM + moviePath

        this.mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(hlsUrl))
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        exoPlayer = SimpleExoPlayer.Builder(this).build().apply {
            playWhenReady = isPlayerPlaying
            seekTo(currentWindow, playbackPosition)
            setMediaItem(mediaItem, false)
            prepare()
        }
        player_view?.player = exoPlayer

        btn_forward?.setOnClickListener {
            player_view?.player?.currentPosition?.plus(10*1000)?.let { it1 ->
                player_view?.player?.seekTo(
                    it1
                )
            }
        }

        btn_backward?.setOnClickListener{
            player_view?.player?.currentPosition?.minus(10*1000)?.let { it1 ->
                player_view?.player?.seekTo(
                    it1
                )
            }
        }
        initFullScreen()
        initPlayerOptions()
    }

    private fun initPlayerOptions(){
        player_view?.player?.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                if(Constants.isNetworkAvailable(this@PlayerActivity)) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> loading?.visibility = View.GONE
                        ExoPlayer.STATE_BUFFERING -> loading?.visibility = View.VISIBLE
                    }
                }else{
                    runError(0)
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                runError(2)
            }
        })
    }


    private fun initFullScreen(){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setFlags(WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE, WindowManager.LayoutParams.ROTATION_ANIMATION_ROTATE)
        supportActionBar?.hide()
    }

    private fun releasePlayer(){
        isPlayerPlaying = exoPlayer.playWhenReady
        playbackPosition = exoPlayer.currentPosition
        currentWindow = exoPlayer.currentWindowIndex
        exoPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, exoPlayer.currentWindowIndex)
        outState.putLong(STATE_RESUME_POSITION, exoPlayer.currentPosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen)
        outState.putBoolean(STATE_PLAYER_PLAYING, isPlayerPlaying)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer()
            if (player_view != null) player_view?.onResume()
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) {
            initPlayer()
            if (player_view != null) player_view?.onResume()
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            if (player_view != null) player_view?.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            if (player_view != null) player_view?.onPause()
            releasePlayer()
        }
    }

    companion object {
        const val STATE_RESUME_WINDOW = "resumeWindow"
        const val STATE_RESUME_POSITION = "resumePosition"
        const val STATE_PLAYER_FULLSCREEN = "playerFullscreen"
        const val STATE_PLAYER_PLAYING = "playerOnPlay"
    }

    private fun runError(type: Int){
        intent.putExtra(Constants.EXTRA_ERRTYPE, type)
        startActivity(Intent(this, ErrorHandlerActivity::class.java))
        finish()
    }
}