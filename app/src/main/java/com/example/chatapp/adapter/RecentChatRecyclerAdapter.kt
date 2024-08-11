package com.example.chatapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.data.model.ChatRoomModel
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.databinding.RecentChatItemBinding
import com.example.chatapp.utils.Extensions
import com.example.chatapp.utils.FireBaseUtils
import com.example.chatapp.views.messages.ChatRoomActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlin.random.Random

class RecentChatRecyclerAdapter(
    options: FirestoreRecyclerOptions<ChatRoomModel>, private val context: Context):
    FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomModelViewHolder {
        val binding = RecentChatItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ChatRoomModelViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onBindViewHolder(holder: ChatRoomModelViewHolder, position: Int, model: ChatRoomModel) {
        FireBaseUtils.getOtherUserFromChatRoom(model.userIds!!).get().addOnCompleteListener { task ->
            if(task.isSuccessful){
                val lastMessageSentByMe: Boolean = model.lastMessageSenderId.equals(FireBaseUtils.currentUserId())

                val otherUserModel = task.result.toObject(UserModel::class.java)
                holder.binding.textItemDisplayName.text = otherUserModel?.username

                val arrayImg = arrayOf(R.drawable.woman,R.drawable.man, R.drawable.man2)
                val randomIndex = Random.nextInt(arrayImg.size)
                val randomDrawable = arrayImg[randomIndex]
                Glide.with(context).load(randomDrawable)
                    .into(holder.binding.imageItemAvatar)

                if(lastMessageSentByMe)
                    holder.binding.textItemLastMessage.text = "You: ${model.lastMessage}"
                else
                holder.binding.textItemLastMessage.text = model.lastMessage
                holder.binding.textTime.text = FireBaseUtils.timestampToString(model.lastMessageTimestamp!!)

                holder.itemView.setOnClickListener{
                    val intent = Intent(context, ChatRoomActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    Extensions.passUserModelAsIntent(intent, otherUserModel!!)
                    context.startActivity(intent)

                }
            }else{
                Log.e("getOtherUserFromChatRoom","error", task.exception)
            }
        }
    }

    class ChatRoomModelViewHolder(val binding: RecentChatItemBinding)
        : RecyclerView.ViewHolder(binding.root)


}