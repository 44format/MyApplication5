package com.example.schooluser.myapplication

import android.net.Uri
import java.net.URL

class Song {
    private var title = ""
    private var artist = ""
    private var filePath = ""
    private var albumpath: Uri? = null
    // Set -  это пути для файлов
    fun setAlbum(path: Uri) {
        albumpath = path
    }

    fun setTitle(path: String) {
        title = path
    }

    fun setArtist(path: String) {
        artist = path
    }

    fun setFilepath(path: String) {
        filePath = path
    }

    fun getFilePath(): String {
        return filePath
    }

    fun getTitle(): String {
        return title
    }

    fun getArtist(): String {
        return artist
    }


}
// гет тайлы возращают их назат