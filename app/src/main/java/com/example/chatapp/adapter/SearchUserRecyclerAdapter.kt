package com.example.chatapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.databinding.ItemAccountBinding
import com.example.chatapp.utils.Extensions
import com.example.chatapp.utils.FireBaseUtils
import com.example.chatapp.views.messages.ChatRoomActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlin.random.Random

class SearchUserRecyclerAdapter(
    options: FirestoreRecyclerOptions<UserModel>, private val context: Context):
    FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserModelViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserModelViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserModelViewHolder, position: Int, model: UserModel) {
        val arrayImg = arrayOf(R.drawable.woman, R.drawable.man, R.drawable.man2)
        val randomIndex = Random.nextInt(arrayImg.size)
        val randomDrawable = arrayImg[randomIndex]
        Glide.with(context).load(randomDrawable)
            .into(holder.binding.imageItemAvatar)
        holder.binding.textItemDisplayName.text = model.username
        holder.binding.textItemLastMessage.text = model.email
        if(model.userId == FireBaseUtils.currentUserId()){
            holder.binding.textItemDisplayName.text = "${model.username} (Me)"
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatRoomActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            Extensions.passUserModelAsIntent(intent, model)
            context.startActivity(intent)

        }



    }

    class UserModelViewHolder(val binding: ItemAccountBinding)
        : RecyclerView.ViewHolder(binding.root)
}