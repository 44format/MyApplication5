package com.example.schooluser.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import com.example.schooluser.myapplication.R.layout.list_element
import com.vanniktech.rxpermission.Permission
import com.vanniktech.rxpermission.RealRxPermission
import kotlinx.android.synthetic.main.list_element.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.await

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //получаем список песен
        val songs = getSongs()
        //проверяем если список песен не пустой то идем дальше в цикл for
        if (songs.isNotEmpty()) {
            //для каждой песни в списке песен выполни то что написанно дальше
            for (song in songs) {
                //возьми элемент который мы сверстали
                val view = LayoutInflater.from(this).inflate(R.layout.list_element, null)
//у него поменяй название песни на то которое мы получили
                view.titlesong.text = song.getTitle()   //getTitle возращает название песни ранее записанной в классе Song
                view.executor.text = song.getArtist() //getArtist
//вешаем на элемент что делать при нажатие на него
                view.onetrack.setOnClickListener {
                    //создаем переменную в которую записавыем в каком активити мы будем переходить и передавать данные и прослушивать
                    val intent = Intent(baseContext, MusicActivity::class.java)
                    // default отправляет задачу в поток который она не будет блокировать
                    GlobalScope.launch(Dispatchers.Default) {
                        // получаем разрешение на чтение и записи внутренней памяти телефона
                        val isGranted = RealRxPermission.getInstance(baseContext)
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .await() //функция остановки которая блокирует главный поток и из-за этого его нужно запускать в default
                                .state() == Permission.State.GRANTED //показывает можно ли нам читать память если true можно а если fаlse нельзя
                        //если разрешение дано выполняет то что внизу
                        if (isGranted) {
                            //передаем активити где будем слушать музыку путь до файла
                            intent.putExtra("FilePath", song.getFilePath())
                            //передаем в активити название файла
                            intent.putExtra("title", song.getTitle())
                            //передаем в активити имя артиста
                            intent.putExtra("artist", song.getArtist())
                            //запускаем новое активити где будем слушать музыку и помещаем в
                            startActivity(intent)
                        }
                    }

                }
                music_menu.addView(view)
            }

        }


    }

    fun getSongs(): List<Song> {
//         Создаем пустой список
        val list = mutableListOf<Song>()
//        получаем ссылку на все файлы которые есть в памяти телефона
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//     собираем все данные которые нам нужно получить для отображения
        val cursorColumns = arrayOf(MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
        )
        // + "=1"  = true + "=0" - false
        val where = MediaStore.Audio.Media.IS_MUSIC + "=1"
        //  здесь мы показываем курсору в каких ячейках памяти нам нужно искать аудио записи
        val cursor = contentResolver.query(uri, cursorColumns, where, null, null)
//пока наш курсор может перемещать на новую ячейку он получает данные
        while (cursor?.moveToNext() == true){
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
            val album_id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
            val albumimage = Uri.parse("content://media/external/audio/albumart")

//         возращает ЮРИ для получения обложки
            val image = ContentUris.withAppendedId(albumimage, album_id)
// withAppend. возращает конкретную обложкую
//        передаем ЮРИ
            // создаем пустой объект класс  сонгб которые будут хранить данные по нашей песне
            val song = Song()
            //используем функцию setAlbum из класса Song что бы в пустое поле картинки нашего альбома записать путь до этой картинки
            song.setAlbum(image)
            //используем функцию  setArtist из класса Song что бы в пустое поле имя артиста записать имя артиста
            song.setArtist(artist)
            // путь файла
            song.setFilepath(data)
            //название песни
            song.setTitle(title)
            //после формирование объекта и записи всех полей мы добавляем нашу песню в список песен
            list.add(song)
        }


//курсор может забирать на себя очень много памяти для этого после того как он отработал мы его выгружаем из памяти с помощью функции close
        cursor?.close()
        return list

    }
}


//   Возвращаем список своих песен
