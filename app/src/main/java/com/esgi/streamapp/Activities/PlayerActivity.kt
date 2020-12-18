@file:Suppress("DEPRECATION")

package com.esgi.streamapp.Activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.esgi.streamapp.Activities.Handler.ErrorHandlerActivity
import com.esgi.streamapp.R
import com.esgi.streamapp.utils.models.Constants
import com.esgi.streamapp.utils.models.ErrorHelper
import com.esgi.streamapp.utils.models.TypeError
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.DefaultHlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.player_custom_control.*
import java.io.Serializable
import java.lang.reflect.Type


class PlayerActivity : AppCompatActivity() {
    private lateinit var playerView : PlayerView
    private lateinit var loading : ProgressBar
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var moviePath: String = ""
    private lateinit var forward : AppCompatImageView
    private lateinit var replay : AppCompatImageView
    private var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_player)
        //Récupération du chemin du film sélectionné.
        intent.extras?.get("path")?.let {
            moviePath = it as String
        }
        isConnected = Constants.isNetworkAvailable(this) == true

        if(!isConnected){
            startActivity(Intent(this@PlayerActivity, ErrorHandlerActivity::class.java))
            val error = ErrorHelper(TypeError.Network, 404, "Vous n'êtes pas connecté à internet.")
            intent.putExtra("error", error)
            finish()
        }

        this.playerView = this.findViewById(R.id.video_view)
        this.loading = this.findViewById(R.id.loading)

        forward = findViewById(R.id.forward)
        replay = findViewById(R.id.replay)

        forward.setOnClickListener{
            playerView.player?.currentPosition?.plus(10*1000)?.let { it1 ->
                playerView.player?.seekTo(
                    it1
                )
            }
        }
        replay.setOnClickListener{
            playerView.player?.currentPosition?.minus(10*1000)?.let { it1 ->
                playerView.player?.seekTo(
                    it1
                )
            }
        }

        initFullScreen()
        playerView.setKeepContentOnPlayerReset(true)
    }

    private fun initFullScreen(){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        val adaptiveTrackSelection: TrackSelection.Factory = AdaptiveTrackSelection.Factory()
        var player = ExoPlayerFactory.newSimpleInstance(applicationContext,
                                                    DefaultTrackSelector(adaptiveTrackSelection),
                                                    DefaultLoadControl())

        playerView.player = player
        val defaultBandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(
            applicationContext,
            Util.getUserAgent(applicationContext, "Exo2"),
            defaultBandwidthMeter
        )

        val hlsDataSourceFactory = DefaultHlsDataSourceFactory(dataSourceFactory)

        val hlsUrl = Constants.URL_STREAM //+ this.moviePath
        val uri: Uri = Uri.parse(hlsUrl)
        var mediaSource: HlsMediaSource = HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(uri)
        player.prepare(mediaSource)

        player.playWhenReady = playWhenReady
        player.addListener(object : Player.EventListener {

            override fun onPlayerStateChanged(playWhenReady: Boolean,playbackState: Int) {
                if(isConnected) {
                    when (playbackState) {
                        ExoPlayer.STATE_READY -> loading.visibility = View.GONE
                        ExoPlayer.STATE_BUFFERING -> loading.visibility = View.VISIBLE
                    }
                }else{
                    startActivity(Intent(this@PlayerActivity, ErrorHandlerActivity::class.java))
                    val error = ErrorHelper(TypeError.Player, 503, "Une erreur s'est produite lors de la lecture. Veuillez réessayer ultérieurement.")
                    intent.putExtra("error", error)
                    releasePlayer()
                    finish()
                }
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                startActivity(Intent(this@PlayerActivity, ErrorHandlerActivity::class.java))
                val error = ErrorHelper(TypeError.Player, 503, "Une erreur s'est produite lors de la lecture. Veuillez réessayer ultérieurement.")
                intent.putExtra("error", error as Serializable)
                releasePlayer()
                finish()
            }
        })
        player.seekTo(currentWindow, playbackPosition)
        player.prepare(mediaSource, true, false)
    }

    private fun releasePlayer() {
        playerView?.let {
            it.player?.let {
                playbackPosition = it.currentPosition
                currentWindow = it.currentWindowIndex
                playWhenReady = it.playWhenReady
                it.release()
            }
            it.player = null
        }
    }

    override fun onPause() {
        super.onPause()
        playerView.player?.pause()
    }

    override fun onStop() {
        super.onStop()
        playerView.player?.pause()
    }

}