package com.example.chatapp.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.chatapp.data.model.UserModel

object Extensions {
    fun Activity.toast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    fun passUserModelAsIntent(intent: Intent, userModel: UserModel){
        intent.putExtra("username", userModel.username)
        intent.putExtra("email", userModel.email)
        intent.putExtra("userId", userModel.userId)
    }

    fun getUserModelFromIntent(intent: Intent, userModel: UserModel): UserModel{
        userModel.username = intent.getStringExtra("username")
        userModel.email = intent.getStringExtra("email")
        userModel.userId = intent.getStringExtra("userId")
        return userModel
    }
}