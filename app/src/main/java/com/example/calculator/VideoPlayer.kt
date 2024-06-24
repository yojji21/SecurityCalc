package com.example.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Log
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.calculator.databinding.ActivityVideoPlayerBinding

class VideoPlayer : AppCompatActivity() {

    lateinit var binding: ActivityVideoPlayerBinding

    lateinit var playerView: PlayerView

    companion object {
        lateinit var player: ExoPlayer
        lateinit var playerList: ArrayList<Video>
        var position: Int = -1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playerView = findViewById(R.id.player_view)

        playerList = ArrayList<Video>()


        if (Sgallery.videoList != null && position >= 0 && position < Sgallery.videoList.size) {
            val video = Sgallery.videoList[position]
            val mediaItem = MediaItem.fromUri(video.artUri)

            player = ExoPlayer.Builder(this).build()
            playerView.player = player

            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        } else {

        }
    }

    override fun onPause() {
        super.onPause()
        player.setPlayWhenReady(false)
    }

    override fun onStop() {
        super.onStop()
        player.stop()
        player.release()
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}