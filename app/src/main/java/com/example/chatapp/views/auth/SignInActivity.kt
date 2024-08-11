package com.example.chatapp.views.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivitySignInBinding
import com.example.chatapp.utils.Extensions.toast
import com.example.chatapp.utils.FireBaseUtils.firebaseAuth


class SignInActivity : AppCompatActivity() {

    lateinit var signInInputsArray: Array<EditText>
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signInInputsArray = arrayOf(binding.etSignInEmail, binding.etSignInPassword)
        binding.btnCreateAccount2.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }
        binding.btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun notEmpty(): Boolean {
        return binding.etSignInEmail.text.isNotEmpty() && binding.etSignInPassword.text.isNotEmpty()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun signIn() {
        val emailUser = binding.etSignInEmail.text.toString()
        val passwordUser = binding.etSignInPassword.text.toString()

            if(notEmpty()){
                firebaseAuth.signInWithEmailAndPassword(emailUser, passwordUser)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){

                            startActivity(Intent(this, CreateUserNameActivity::class.java))
                            toast("Successfully sign in")
                            finish()
                        }else{
                            toast("Sign in failed")
                        }

                    }
            }else{
                for (item in signInInputsArray){
                    if(item.text.toString().trim().isEmpty()){
                        item.error = "${item.hint} is required"
                    }
                }
            }
        }



}
