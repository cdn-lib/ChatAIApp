package com.chatai.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatai.app.R
import com.chatai.app.model.ChatMessage
import io.noties.markwon.Markwon
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).role == "user") VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_user, parent, false)
            UserViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_ai, parent, false)
            AiViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is UserViewHolder -> holder.bind(message)
            is AiViewHolder -> holder.bind(message)
        }
    }

    // ===== USER ViewHolder =====
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)

        fun bind(message: ChatMessage) {
            tvMessage.text = message.content
            tvTime.text = formatTime(message.timestamp)

            itemView.setOnLongClickListener {
                copyToClipboard(itemView, message.content)
                true
            }
        }
    }

    // ===== AI ViewHolder =====
    inner class AiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val loadingDots: View = itemView.findViewById(R.id.loadingDots)
        private val markwon: Markwon = Markwon.create(itemView.context)

        fun bind(message: ChatMessage) {
            if (message.isLoading) {
                tvMessage.visibility = View.GONE
                loadingDots.visibility = View.VISIBLE
                tvTime.visibility = View.GONE
            } else {
                tvMessage.visibility = View.VISIBLE
                loadingDots.visibility = View.GONE
                tvTime.visibility = View.VISIBLE
                markwon.setMarkdown(tvMessage, message.content)
                tvTime.text = formatTime(message.timestamp)

                itemView.setOnLongClickListener {
                    copyToClipboard(itemView, message.content)
                    true
                }
            }
        }
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun copyToClipboard(view: View, text: String) {
        val clipboard = view.context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("AI Message", text)
        clipboard.setPrimaryClip(clip)
        android.widget.Toast.makeText(view.context, "Pesan disalin!", android.widget.Toast.LENGTH_SHORT).show()
    }

    class DiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
            oldItem.timestamp == newItem.timestamp && oldItem.role == newItem.role

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage) =
            oldItem == newItem
    }
}
