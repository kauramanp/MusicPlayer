package com.aman.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aman.musicplayer.databinding.FragmentMusicPlayerBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
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

        }

        binding.btnBackward.setOnClickListener {

        }
    }

    fun updateView(){
        if(mainActivity.mediaPlayer.isPlaying){
            binding.btnPlay.setImageResource(R.drawable.ic_pause)
            binding.musicContent = mainActivity.musicContent
        }else{
            binding.btnPlay.setImageResource(R.drawable.ic_play)
        }

        binding.disableFirst = mainActivity.currentPlayingPosition == 0
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