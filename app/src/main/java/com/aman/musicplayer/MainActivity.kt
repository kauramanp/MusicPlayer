package com.aman.musicplayer

import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.aman.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            //songs
            getSongs()
        }else{
            //alert
            //go to setting
            //exit finish
        }
    }

    var musicList = ArrayList<MusicContent>()
    lateinit var musicViewModel: MusicViewModel
    var mediaPlayer: MediaPlayer = MediaPlayer()
    var musicContent: MusicContent ?= null
    var currentPlayingPosition = 0
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        musicViewModel = ViewModelProvider(this)[MusicViewModel::class.java]
        navController = findNavController(R.id.navController)
        binding.bottomBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottomPlaylist -> navController.navigate(R.id.playlistFragment)
                R.id.bottomMusicPlayer -> navController.navigate(R.id.playMusicFragment)
            }
            return@setOnItemSelectedListener true
        }

        navController.addOnDestinationChangedListener{ _, destinationId, _ ->
            when(destinationId.id){
                R.id.playlistFragment-> binding.bottomBar.menu.getItem(0).isChecked = true
                R.id.playMusicFragment-> binding.bottomBar.menu.getItem(1).isChecked = true
            }
        }

        mediaPlayer.setOnCompletionListener {
            if(currentPlayingPosition < musicList.size - 2){
                musicList[currentPlayingPosition].isPlaying = false
                currentPlayingPosition++
                changeSong()
            }
        }
    }

    fun changeSong(){
        mediaPlayer.stop()
        mediaPlayer.reset()
        musicContent = musicList[currentPlayingPosition]
        musicList[currentPlayingPosition].isPlaying = true
        mediaPlayer.setDataSource(this, Uri.parse(musicContent?.storageLocation))
        mediaPlayer.prepare()
        mediaPlayer.start()
        musicViewModel.musicContentList.value = musicList
        musicViewModel.updateView.value = true

    }

    override fun onResume() {
        super.onResume()
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED)
        {
            getSongs()
        }else{
            permission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    fun getSongs(){
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC
        val cursor: Cursor? = contentResolver?.query(uri, null,
            selection, null, null)
        if(cursor != null) {
            if (cursor?.moveToFirst() == true) {
                while(cursor.isLast == false) {
                    musicList.add(
                        MusicContent(
                            title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                            artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            storageLocation = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        )
                    )
                    cursor.moveToNext()
                }
            }
            musicViewModel.musicContentList.value = musicList

        }

    }
}