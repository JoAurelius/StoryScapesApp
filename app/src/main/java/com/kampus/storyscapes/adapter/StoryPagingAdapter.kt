
package com.kampus.storyscapes.adapter

import android.content.ClipData.Item
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kampus.storyscapes.activity.DetailStoryActivity
import com.kampus.storyscapes.activity.loadImage
import com.kampus.storyscapes.databinding.ItemStoryBinding
import com.kampus.storyscapes.model.Story

class StoryListAdapter(
    private val token : String
) : PagingDataAdapter<Story, StoryListAdapter.ListViewHolder>(DIFF_CALLBACK) {
    inner class ListViewHolder(
        private var binding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                Log.d("ADAPTER","BINDING")
                this.ivItemPhoto.loadImage(story.photoUrl, itemView.context )
                this.tvItemName.text = story.name
                this.tvItemDescription.text = story.description
            }
            itemView.setOnClickListener {
                itemView.setOnClickListener {
                    val intentDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                    intentDetail.putExtra(DetailStoryActivity.ID, story.id)
                    intentDetail.putExtra(DetailStoryActivity.TOKEN, token)
                    itemView.context.startActivity(intentDetail)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val storyItem = getItem(position)
        if (storyItem != null) {
            holder.bind(storyItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}