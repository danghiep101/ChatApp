package com.example.chatapp.views.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.utils.FireBaseUtils.firebaseAuth
import com.example.chatapp.views.menu.MenuActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = firebaseAuth.currentUser
        val intent = if (currentUser != null) {
            Intent(this, MenuActivity::class.java)
        } else {
            Intent(this, SignInActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}