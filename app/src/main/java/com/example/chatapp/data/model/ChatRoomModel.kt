package com.example.chatapp.data.model

import com.google.firebase.Timestamp

data class ChatRoomModel(
    val chatRoomId: String? = null,
    val userIds: List<String>? =null,
    var lastMessageTimestamp: Timestamp? =null,
    var lastMessageSenderId: String? = null,
    var lastMessage: String?= null,
)
