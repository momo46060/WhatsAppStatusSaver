package com.bomb.app.whatsappstatussaver.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.drawToBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bomb.app.whatsappstatussaver.R
import com.bomb.app.whatsappstatussaver.ads.Adsimpl
import com.bomb.app.whatsappstatussaver.ui.DownloadFragmentDirections
import com.bomb.app.whatsappstatussaver.ui.SavedFragmentDirections
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScoped
class WhatsAdapter @Inject constructor(@ApplicationContext private val context: Context,private val adsimpl: Adsimpl,private val adapterFunImp: AdapterFunImp) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var galleryList: List<String> = ArrayList()
    private var image = 0
    private var video = 1

    fun setlist(galleryList: List<String>) {
        this.galleryList = galleryList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (galleryList[position].last() == '4') {
            video
        } else {
            image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == video) {
            val menuItemLayoutView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_item, parent, false)
            VideoViewHolder(menuItemLayoutView)
        } else {
            val menuItemLayoutView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
            ImageViewHolder(menuItemLayoutView)
        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {

                val path = galleryList[position]
                Glide.with(context).load(path).into(holder.imageView)
                holder.imageView.setOnClickListener {
                        try {
                            it.findNavController().navigate(
                                SavedFragmentDirections.actionSavedFragmentToShowVideoFragment(path,true))
                            adsimpl.showad()
                        }catch (e:Exception){
                            it.findNavController().navigate(
                                DownloadFragmentDirections.actionDownloadFragmentToShowVideoFragment(path,false))
                            adsimpl.showad()
                        }
                }

        } else if (holder is VideoViewHolder) {
                val path = galleryList[position]
                Glide.with(context).load(path).into(holder.videoView)
                holder.videoView.setOnClickListener {
                    try {
                        it.findNavController().navigate(
                            DownloadFragmentDirections.actionDownloadFragmentToShowVideoFragment(path,false))
                        adsimpl.showad()
                    }catch (e:Exception){
                        it.findNavController().navigate(
                            SavedFragmentDirections.actionSavedFragmentToShowVideoFragment(path,true))
                        adsimpl.showad()
                    }
                }
        }
    }




    override fun getItemCount(): Int {
        return galleryList.size
    }




    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image)


    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoView: ImageView = itemView.findViewById(R.id.video)

    }

}
