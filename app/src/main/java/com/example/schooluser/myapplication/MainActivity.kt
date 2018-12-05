package com.example.schooluser.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath + "/track.mp3"
        setContentView(R.layout.activity_main)
        button.setOnClickListener {


            GlobalScope.launch(Dispatchers.Default) {

                // разрешения
                val isGranted = RealRxPermission.getInstance(baseContext)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .await()
                    .state() == Permission.State.GRANTED

                if (isGranted) {
                    val intent = Intent(baseContext, MusicActivity::class.java)
                    intent.putExtra("path",filePath)
                    startActivity(intent)    }

            }
        }
    }

}
