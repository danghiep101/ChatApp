package com.example.chatapp.views.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.chatapp.adapter.ChatRecyclerAdapter

import com.example.chatapp.data.model.ChatRoomModel
import com.example.chatapp.data.model.MessageModel
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.databinding.ActivityChatRoomBinding

import com.example.chatapp.utils.Extensions
import com.example.chatapp.utils.FireBaseUtils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query


class ChatRoomActivity : AppCompatActivity() {
    private var otherUser = UserModel()
    private lateinit var chatRoomId: String
    private lateinit var binding: ActivityChatRoomBinding
    private var chatRoomModel: ChatRoomModel? = null
    private var adapter: ChatRecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        otherUser = Extensions.getUserModelFromIntent(intent, userModel = UserModel())
        chatRoomId =
            FireBaseUtils.getChatRoomId(FireBaseUtils.currentUserId()!!, otherUser.userId!!)

        binding.tvUsername.text = otherUser.username

        getOrCreateChatroomModel()
        setUpChatRecycleView()
        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isEmpty())
                return@setOnClickListener
            sendMessageToUser(message)
        }
    }

    private fun setUpChatRecycleView() {
        val query = FireBaseUtils.getChatRomMessageRefrence(chatRoomId)
            .orderBy("timestamp", Query.Direction.DESCENDING)


        val options = FirestoreRecyclerOptions.Builder<MessageModel>()
            .setQuery(query, MessageModel::class.java).build()

        adapter = ChatRecyclerAdapter(options, applicationContext)
        val manager = LinearLayoutManager(this)
        manager.reverseLayout = true
        binding.recycleViewMessage.layoutManager = manager
        binding.recycleViewMessage.adapter = adapter
        adapter!!.startListening()
        adapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.recycleViewMessage.smoothScrollToPosition(0)
            }
        })
    }


    @SuppressLint("SetTextI18n")
    private fun sendMessageToUser(message: String) {
        chatRoomModel?.let {
            it.lastMessageTimestamp = Timestamp.now()
            it.lastMessageSenderId = FireBaseUtils.currentUserId()
            it.lastMessage = message

            FireBaseUtils.getChatRoomReference(chatRoomId).set(it)

            val messageModel = MessageModel(message, FireBaseUtils.currentUserId(), Timestamp.now())


            FireBaseUtils.getChatRomMessageRefrence(chatRoomId).add(messageModel)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
        
                        binding.etMessage.setText("")
                    }
                }
        } ?: run {
            Log.e("sendMessageToUser", "Chat room model is null")
        }
    }

    private fun getOrCreateChatroomModel() {
        FireBaseUtils.getChatRoomReference(chatRoomId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                chatRoomModel = task.result.toObject(ChatRoomModel::class.java)
                if (chatRoomModel == null) {
                    // First time chat
                    chatRoomModel = ChatRoomModel(
                        chatRoomId,
                        listOf(FireBaseUtils.currentUserId()!!, otherUser.userId!!),
                        Timestamp.now(),
                        ""
                    )
                    FireBaseUtils.getChatRoomReference(chatRoomId).set(chatRoomModel!!)
                }
            }
        }
    }

}