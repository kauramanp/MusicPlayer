package com.aman.musicplayer

/**
 * @Author: 017
 * @Date: 09/09/23
 * @Time: 4:45 pm
 */
data class MusicContent(
    var title: String = "",
    var artistName: String = "",
    var duration: String = "",
    var storageLocation: String="",
    var isPlaying: Boolean = false
)
