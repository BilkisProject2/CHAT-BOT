package com.example.instagrame.ui.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instagrame.R
import com.example.instagrame.ui.Data.Message
import com.example.instagrame.ui.Util.Constaints.RECEIVE_ID
import com.example.instagrame.ui.Util.Constaints.SEND_ID

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageingViewHolder>() {
    var messageList = mutableListOf<Message>()

    inner class MessageingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                messageList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageingViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.messagesample, parent, false)
        return MessageingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageingViewHolder, position: Int) {
        val currentMessage = messageList[position]
        when (currentMessage.id) {
            SEND_ID -> {
                holder.itemView.findViewById<TextView>(R.id.tv_message).apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.findViewById<TextView>(R.id.tv_bot_message).visibility = View.GONE
            }

            RECEIVE_ID -> {
                holder.itemView.findViewById<TextView>(R.id.tv_bot_message).apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                holder.itemView.findViewById<TextView>(R.id.tv_message).visibility = View.GONE
            }
        }
    }

    fun insertMessage(message: Message){
        this.messageList.add(message)
        notifyItemInserted(messageList.size)
        notifyDataSetChanged()
    }

}
