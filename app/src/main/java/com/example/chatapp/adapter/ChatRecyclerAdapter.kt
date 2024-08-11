package com.example.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.data.model.MessageModel
import com.example.chatapp.databinding.ChatMessageRecycleBinding
import com.example.chatapp.utils.FireBaseUtils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<MessageModel>, private val context: Context
):
    FirestoreRecyclerAdapter<MessageModel, ChatRecyclerAdapter.MessageModelViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageModelViewHolder {
        val binding = ChatMessageRecycleBinding.inflate(LayoutInflater.from(context), parent, false)
        return MessageModelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageModelViewHolder, position: Int, model: MessageModel) {
        if(model.senderId.equals(FireBaseUtils.currentUserId())){
            holder.binding.leftChatLayout.visibility = View.GONE
            holder.binding.rightChatLayout.visibility = View.VISIBLE
            holder.binding.rightChatTextview.setText(model.message)
        }else{
            holder.binding.rightChatLayout.visibility = View.GONE
            holder.binding.leftChatLayout.visibility = View.VISIBLE
            holder.binding.leftChatTextview.setText(model.message)
        }



    }

    class MessageModelViewHolder(val binding: ChatMessageRecycleBinding)
        : RecyclerView.ViewHolder(binding.root) {

    }
}