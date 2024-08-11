package com.example.chatapp.views.menu


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.example.chatapp.R
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.databinding.ActivityMenuBinding
import com.example.chatapp.utils.Extensions.toast
import com.example.chatapp.utils.FireBaseUtils
import com.example.chatapp.utils.FireBaseUtils.firebaseAuth
import com.example.chatapp.views.auth.SignInActivity
import com.example.chatapp.views.call.CallFragment
import com.example.chatapp.views.messages.ChatFragment

import com.example.chatapp.views.messages.SearchUserAcitivty
import com.google.firebase.analytics.FirebaseAnalytics

private var mFirebaseAnalytics: FirebaseAnalytics? = null

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var navController: NavController
    private var userModel : UserModel? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null){
            val chatFragment = ChatFragment()
            supportFragmentManager.beginTransaction().replace(binding.fragmentConView.id, chatFragment)
                .commit()
        }
        getUserName()
        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_chat -> {
                    val callFragment = ChatFragment()
                    supportFragmentManager.beginTransaction().replace(binding.fragmentConView.id, callFragment)
                        .commit()
                    return@setOnItemSelectedListener true

                }
                R.id.item_call -> {
                    val callFragment = CallFragment()
                    supportFragmentManager.beginTransaction().replace(binding.fragmentConView.id, callFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.item_sign_out -> {
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
                    firebaseAuth.signOut()
                    Log.d("Auth", "User signed out: ${firebaseAuth.currentUser}")
                    startActivity(Intent(this, SignInActivity::class.java))
                    toast("Signed out")
                    finish()
                    return@setOnItemSelectedListener true
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)

                }

            }
        }

        binding.btnSearch.setOnClickListener{
            startActivity(Intent(this@MenuActivity, SearchUserAcitivty::class.java))
        }


    }

    private fun getUserName() {
        FireBaseUtils.currentUserDetails().get().addOnCompleteListener { task  ->
            if (task.isSuccessful){
                userModel = task.result.toObject(UserModel::class.java)
                userModel?.let {
                    binding.textUsername.text = it.username
                }
            }
        }
    }
}