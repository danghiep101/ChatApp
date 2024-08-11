package com.example.chatapp.data.model

import com.google.firebase.Timestamp

data class MessageModel (
    val message: String? = null,
    val senderId: String ? = null,
    val timestamp: Timestamp ? = null
)