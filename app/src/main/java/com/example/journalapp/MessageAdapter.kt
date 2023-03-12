package com.example.journalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    var messageList: List<Message>
):RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val chatView = LayoutInflater.from(parent.context).inflate(
            R.layout.chat_item,
            parent,
            false
        )

        return MessageViewHolder(chatView)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message:Message = messageList[position]

        if(message.getSentBy()==message.SENT_BY_ME){
            holder.leftChatView.visibility = View.GONE
            holder.rightChatView.visibility = View.VISIBLE

            holder.rightTextView.text = message.getMessage()


        }else{
            holder.rightChatView.visibility = View.GONE
            holder.leftChatView.visibility = View.VISIBLE

            holder.leftTextView.text = message.getMessage()
        }
    }


    class MessageViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val leftChatView:LinearLayout = itemView.findViewById(R.id.left_chat_view)
        val rightChatView:LinearLayout = itemView.findViewById(R.id.right_chat_view)

        val leftTextView:TextView = itemView.findViewById(R.id.left_chat_text_view)
        val rightTextView:TextView = itemView.findViewById(R.id.right_chat_text_view)

    }

}