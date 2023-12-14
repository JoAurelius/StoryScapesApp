package com.kampus.storyscapes.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kampus.storyscapes.R
import com.kampus.storyscapes.activity.DetailStoryActivity
import com.kampus.storyscapes.model.Story

class StoryAdapter(private val listStory: List<Story>, private val token : String) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false))

    override fun onBindViewHolder(holder: StoryAdapter.ViewHolder, position: Int) {
        val story = listStory[position]
        holder.tvTitle.text = story.name
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.imgItem)
        holder.tvDesc.text = story.description
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentDetail.putExtra(DetailStoryActivity.ID, story.id)
            intentDetail.putExtra(DetailStoryActivity.TOKEN, token)
            holder.itemView.context.startActivity(intentDetail)
        }

    }

    override fun getItemCount(): Int = listStory.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_item_name)
        val imgItem: ImageView = view.findViewById(R.id.iv_item_photo)
        val tvDesc: TextView = view.findViewById(R.id.tv_item_description)
    }
}