package com.example.schooluser.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_music.*

class MusicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        //получили из другого активити путь до файла
        val path = intent.getStringExtra("FilePath")
        //получили из другого активити название этого файла
        val title = intent.getStringExtra("title")
        //создали плеер
        val player = MediaPlayer()
        //поместили в этот плеер путь который получили сверху
        player.setDataSource(path)
       //запустили плеер,музыку играет
        player.prepare()
        var playing = false
        play.setOnClickListener {
            if (playing == true) {
                player.pause()
                playing = false
                play.setImageResource(R.drawable.ic_play)

            } else {
                player.start()
                playing = true
                play.setImageResource(R.drawable.ic_pause)
            }

        }

        back.setOnClickListener {
            player.stop()
            player.prepare()
            if (playing == true) {
                player.start()
            }


        }
        rewing.setOnClickListener {
            player.seekTo(player.currentPosition + 5000)
            if (playing == true) {
            }
        }
        back.setOnClickListener {
            player.seekTo(player.currentPosition - 5000)
            if (playing == true) {
            }
        }
    }
}