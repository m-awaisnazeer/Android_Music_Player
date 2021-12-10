package com.communisolve.musicplayer.adapter

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.communisolve.musicplayer.R
import com.communisolve.musicplayer.model.MusicFiles

class MusicAdapter(
    val mFiles: ArrayList<MusicFiles>
) : RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val music_img: ImageView = itemView.findViewById(R.id.music_img)
        val music_file_name: TextView = itemView.findViewById(R.id.music_file_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.music_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.music_file_name.text = mFiles.get(position).title
        val image = getAlbumArt(mFiles.get(position).path)
        if (image != null) {
            Glide.with(holder.itemView.context).asBitmap().load(image).into(holder.music_img)
        } else {
            Glide.with(holder.itemView.context)
                .load(holder.itemView.context.getDrawable(R.mipmap.ic_launcher_round))
                .into(holder.music_img)

        }
    }

    override fun getItemCount(): Int {
        return mFiles.size
    }

    fun getAlbumArt(uri: String): ByteArray? {
        val retriever: MediaMetadataRetriever = MediaMetadataRetriever()
        retriever.setDataSource(uri)
        val art = retriever.embeddedPicture
        retriever.release()
        return art
    }
}