package com.example.chatapp.views.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.databinding.ActivityCreateUserNameBinding
import com.example.chatapp.utils.FireBaseUtils
import com.example.chatapp.views.menu.MenuActivity
import com.google.firebase.Timestamp

class CreateUserNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateUserNameBinding
    private var userModel: UserModel? = null  // Change lateinit var to nullable var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserName()
        binding.btnConfirm.setOnClickListener {
            setUsername()
        }
    }

    private fun setUsername() {
        val username = binding.etUsername.text.toString()
        if (username.isEmpty() || username.length < 3) {
            binding.etUsername.error = "Username length should be at least 3 chars"
            return
        }else{
            if (userModel != null) {
                userModel?.username = username
            } else {
                userModel = UserModel(
                    email = FireBaseUtils.firebaseUser?.email,
                    password = "null",
                    username = username,
                    createdTimestamp = Timestamp.now(),
                    userId = FireBaseUtils.currentUserId()
                )
            }
        }

        FireBaseUtils.currentUserDetails().set(userModel!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@CreateUserNameActivity, MenuActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }else{
                Log.e("CreateUserNameActivity", "Error setting user details", task.exception)
            }
        }
    }
    private fun getUserName() {
        FireBaseUtils.currentUserDetails().get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userModel = task.result.toObject(UserModel::class.java)  // Initialize user here
                userModel?.let {
                    binding.etUsername.setText(it.username)
                }
            }else{
                Log.e("CreateUserNameActivity", "Error getting user details", task.exception)
            }
        }
    }
}




