package org.hilfulfujul.allajhari.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.hilfulfujul.allajhari.R
import org.hilfulfujul.allajhari.books.Book
import org.hilfulfujul.allajhari.databinding.ItemBookBinding

class BooksAdapter(
    private val itemClickListener: ((Book) -> Unit)? = null
) : ListAdapter<Book, BooksAdapter.BookViewHolder>(BooksDiffCallback()) {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemBookBinding.bind(itemView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.invoke(getItem(position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = getItem(position)
        holder.binding.bookName.text = currentBook.name
        Glide.with(holder.itemView.context).load(
            currentBook.cover
        ).into(holder.binding.bookCover)
    }

    class BooksDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.bid == newItem.bid // Assuming name is unique identifier
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}