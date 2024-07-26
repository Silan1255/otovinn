package com.example.otovinncase.ui.main.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otovinncase.R
import com.example.otovinncase.data.model.Station

class StationsAdapter(
    private var stations: ArrayList<Station>,
    private val context: Context,
) : RecyclerView.Adapter<StationsAdapter.StationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_station_list, parent, false)
        return StationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {
        val story = stations[position]
        Glide.with(context).load(story.image).into(holder.stationImage)
        story.name?.let { holder.titleStation.text = it }
        holder.ratingBar.rating = story.point!!.toFloat()
        val progressDrawable = holder.ratingBar.progressDrawable
        progressDrawable.setTint(Color.GREEN)
    }

    override fun getItemCount(): Int = stations.size

    inner class StationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var stationImage: ImageView = itemView.findViewById(R.id.img_station)
        var titleStation: TextView = itemView.findViewById(R.id.txt_title)
        var ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

    }
}

