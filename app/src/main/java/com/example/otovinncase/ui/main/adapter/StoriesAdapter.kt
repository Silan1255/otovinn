package com.example.otovinncase.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otovinncase.R
import com.example.otovinncase.data.model.Story

class StoriesAdapter(
    private var stories: ArrayList<Story>,
    private val context: Context,
) : RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_story_list, parent, false)
        return StoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val story = stories[position]
        Glide.with(context).load(story.thumb).into(holder.storyImage)
    }

    override fun getItemCount(): Int = stories.size

    inner class StoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var storyImage: ImageView = itemView.findViewById(R.id.img_story)
    }
}

