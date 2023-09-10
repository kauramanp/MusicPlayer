package com.aman.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aman.musicplayer.databinding.FragmentMusicPlayerBinding
import java.util.concurrent.TimeUnit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MusicPlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicPlayerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMusicPlayerBinding
    lateinit var mainActivity : MainActivity
    lateinit var musicViewModel: MusicViewModel
    var handler : Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        musicViewModel = ViewModelProvider(mainActivity)[MusicViewModel::class.java]
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateView()

        musicViewModel.updateView.observe(mainActivity){
            updateView()
        }

        binding.btnPlay.setOnClickListener {
            if(mainActivity.mediaPlayer.isPlaying){
                mainActivity.mediaPlayer.pause()
            }else if(mainActivity.musicContent != null){
                mainActivity.mediaPlayer = MediaPlayer.create(mainActivity, Uri.parse( mainActivity.musicContent?.storageLocation))
                mainActivity.mediaPlayer.start()

            }
            updateView()
        }

        binding.btnForward.setOnClickListener {
            playNextSong()
        }

        binding.btnBackward.setOnClickListener {
            if(mainActivity.currentPlayingPosition > 1){
                mainActivity.musicList[mainActivity.currentPlayingPosition].isPlaying = false
                mainActivity.currentPlayingPosition--
                mainActivity.changeSong()
            }

        }

        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mainActivity.mediaPlayer != null && fromUser) {
                    mainActivity.mediaPlayer.seekTo(progress * 1000)
                }
            }
        })
    }

    private fun playNextSong(){
        if(mainActivity.currentPlayingPosition < (mainActivity.musicList.size-2)){
            mainActivity.musicList[mainActivity.currentPlayingPosition].isPlaying = false
            mainActivity.currentPlayingPosition++
            mainActivity.changeSong()
        }
    }



    fun updateView(){
        if(mainActivity.mediaPlayer.isPlaying){
            binding.btnPlay.setImageResource(R.drawable.ic_pause)
            binding.musicContent = mainActivity.musicContent
            handler.removeCallbacks(moveSeekBarThread)
            handler.postDelayed(moveSeekBarThread, 500)
            binding.endTime?.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(mainActivity.mediaPlayer.duration.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(mainActivity.mediaPlayer.duration.toLong()) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(mainActivity.mediaPlayer.duration.toLong()))))


        }else{
            binding.btnPlay.setImageResource(R.drawable.ic_play)
        }

        binding.disableFirst = mainActivity.currentPlayingPosition == 0
    }


    private val moveSeekBarThread: Runnable = object : Runnable {
        override fun run() {
            if (mainActivity.mediaPlayer.isPlaying()) {
                val mediaPosNew: Int = mainActivity.mediaPlayer.currentPosition
                val mediaMaxNew: Int = mainActivity.mediaPlayer.duration
                binding.startTime?.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(mediaPosNew.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(mediaPosNew.toLong()) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(mediaPosNew.toLong()))))

                binding.seekBar.max = mediaMaxNew
                binding.seekBar.progress = mediaPosNew
                handler.postDelayed(this, 500) //Looping the thread after 0.1 second
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MusicPlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MusicPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}