package com.example.otovinncase.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otovinncase.R
import com.example.otovinncase.data.model.Slider

class SliderAdapter(
    private val sliders: ArrayList<Slider>,
    private val context: Context
    ) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_slider_list, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val slider = sliders[position]
        Glide.with(holder.itemView.context)
            .load(slider.url)
            .into(holder.slider)
    }

    override fun getItemCount(): Int = sliders.size

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var slider: ImageView = itemView.findViewById(R.id.img_slider)
    }
}

