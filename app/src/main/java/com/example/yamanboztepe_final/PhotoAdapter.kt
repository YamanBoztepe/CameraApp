package com.example.yamanboztepe_final

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class PhotoAdapter(val photos: ArrayList<TaggedPhotos>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class PhotoHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgRecyclerPhoto: ImageView = itemView.findViewById(R.id.imgRecyclerPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item,parent,false)
        return PhotoHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = photos.get(position)
        if (holder is PhotoHolder) {
            holder.imgRecyclerPhoto.load(photo.photos)
        }

    }

    override fun getItemCount(): Int {
        return photos.size
    }
}