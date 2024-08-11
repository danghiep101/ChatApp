package com.example.chatapp.views.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivityCreateAccountBinding
import com.example.chatapp.utils.Extensions.toast
import com.example.chatapp.utils.FireBaseUtils.firebaseAuth


class CreateAccountActivity : AppCompatActivity() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private lateinit var createAccountInputsArray: Array<EditText>
    private lateinit var binding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createAccountInputsArray =
            arrayOf(binding.etEmail, binding.etPassword, binding.etConfirmPassword)
        binding.btnCreateAccount.setOnClickListener {
            signUp()
        }

        binding.btnConfirm.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            toast("please sign into your account")
            finish()
        }
    }

    private fun signUp() {
         userEmail = binding.etEmail.text.toString().trim()
        userPassword = binding.etPassword.text.toString()
        if (passwordChecked()) {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        startActivity(Intent(this, CreateUserNameActivity::class.java))
                        toast("Please fill your username")
                    }else{
                        toast("Sign up failed")
                    }
                }
        }


    }

    private fun passwordChecked(): Boolean {
        var checkStatus = false
        if (notEmpty()) {
            if (binding.etPassword.text.toString().trim() == binding.etConfirmPassword.text.toString().trim()) {
                checkStatus = true
            }else{
                toast("Password are not the same")
            }
        }else {
            for (item in createAccountInputsArray){
                item.error = "${item.hint} is required"
                checkStatus = false
            }
        }
        return  checkStatus

    }

    private fun notEmpty(): Boolean {
        return binding.etEmail.text.toString().isNotEmpty() &&
                binding.etPassword.text.toString().isNotEmpty() &&
                binding.etConfirmPassword.text.toString().isNotEmpty()
    }

}