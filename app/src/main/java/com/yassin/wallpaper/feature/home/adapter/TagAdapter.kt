package com.yassin.wallpaper.feature.home.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sfaxdroid.data.mappers.TagView
import com.yassin.wallpaper.R

class TagAdapter(
    private val mTagList: List<TagView>,
    private val clickListener: (TagView) -> Unit
) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    var selectedItemPos = -1
    var lastItemSelectedPos = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TagViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.inflate_tag,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bindView(mTagList[position], mTagList.size - 1, clickListener)
    }

    override fun getItemCount() = mTagList.size

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mTxtLabel: TextView = itemView.findViewById(R.id.item_content)
        private val mDivider: View = itemView.findViewById(R.id.item_divider)
        private val mLayout: LinearLayout = itemView.findViewById(R.id.item_tag_main)

        fun bindView(tagView: TagView, tagSize: Int, clickListener: (TagView) -> Unit) {
            mDivider.visibility = if (position == tagSize) View.INVISIBLE else View.VISIBLE
            mTxtLabel.text = tagView.name
            mLayout.setOnClickListener { clickListener(tagView) }
        }

        private fun defaultBg() {
            itemView.background =
                itemView.context.getDrawable(R.drawable.round_corner_tag_unselected)
        }

        private fun selectedBg() {
            itemView.background = itemView.context.getDrawable(R.drawable.round_corner_tag_selected)
        }

    }
}