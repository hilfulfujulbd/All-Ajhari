package org.hilfulfujul.allajhari.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.hilfulfujul.allajhari.R
import org.hilfulfujul.allajhari.books.Chapter
import org.hilfulfujul.allajhari.databinding.ItemBookChapterBinding

class ChapterAdapter(
    private val itemClickListener: ((Chapter) -> Unit)? = null
) : ListAdapter<Chapter, ChapterAdapter.ChapterViewHolder>(BooksDiffCallback()) {

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemBookChapterBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.invoke(getItem(position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book_chapter, parent, false)
        return ChapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapterBook = getItem(position)
        holder.binding.chapterTitle.text = chapterBook.title
    }

    class BooksDiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
            return oldItem == newItem
        }
    }
}