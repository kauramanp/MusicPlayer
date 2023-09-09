package com.aman.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aman.musicplayer.databinding.RecyclerviewBinding

interface MusicClick{
    fun OnSongPlayClick(musicContent: MusicContent, position: Int)
}
class RecyclerAdapter(var musicClick : MusicClick): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var musicContent: ArrayList<MusicContent> = arrayListOf()
    class ViewHolder(var view: RecyclerviewBinding): RecyclerView.ViewHolder(view.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = RecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = musicContent.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.musicContent = musicContent[position]
        holder.view.musicClick = musicClick
        holder.view.position = position
    }

    fun updateList( musicContent: ArrayList<MusicContent>){
        this.musicContent.clear()
        this.musicContent.addAll(musicContent)
        notifyDataSetChanged()
    }

}