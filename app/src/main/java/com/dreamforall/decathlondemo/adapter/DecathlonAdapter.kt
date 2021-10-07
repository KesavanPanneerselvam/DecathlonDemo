package com.dreamforall.decathlondemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.dreamforall.decathlondemo.R
import com.dreamforall.decathlondemo.databinding.ItemSportsViewBinding


class DecathlonAdapter(
    private val arrayList: List<DataSportList>,
    private val context: Context,
    private val listener: ItemClickListener
) : RecyclerView.Adapter<DecathlonAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecathlonAdapter.ViewHolder {
        val binding =
            ItemSportsViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DecathlonAdapter.ViewHolder, position: Int) {
        holder.txtSportsTitle.text = arrayList[position].attributes.name
        holder.txtSportsDescription.text = arrayList[position].attributes.description

        val url = arrayList[position].attributes.icon

        if (url?.isNotEmpty() == true) {
            holder.imgSportsIcon.loadUrl(url)
        }

        holder.txtSportsDescription.setOnClickListener {
            itemClicked(arrayList[position], listener)
        }
        holder.txtSportsTitle.setOnClickListener {
            itemClicked(arrayList[position], listener)
        }
        holder.imgSportsIcon.setOnClickListener {
            itemClicked(arrayList[position], listener)
        }
        holder.cardContainer.setOnClickListener {
            itemClicked(arrayList[position], listener)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(itemView: ItemSportsViewBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val txtSportsTitle = itemView.txtSportsTitle
        val txtSportsDescription = itemView.txtSportsDescription
        val imgSportsIcon = itemView.imgSportsIcon
        val cardContainer = itemView.cardContainer
    }

    fun itemClicked(dataSportList: DataSportList, listener: ItemClickListener) {
        listener.OnItemClicked(dataSportList)
    }

    fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .placeholder(R.drawable.icbroken_image_black)
            .error(R.drawable.icbroken_image_black)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }
}