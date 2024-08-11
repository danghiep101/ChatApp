package com.example.chatapp.data.model

import com.google.firebase.Timestamp

data class UserModel(
    var email: String? = null,
    var password: String? = "null",
    var username: String? = null,
    var createdTimestamp: Timestamp? = null,
    var userId: String? = null,
    var fcmToken: String? = null

)

