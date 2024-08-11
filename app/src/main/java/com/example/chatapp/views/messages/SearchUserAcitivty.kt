package com.example.chatapp.views.messages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.SearchUserRecyclerAdapter
import com.example.chatapp.data.model.UserModel
import com.example.chatapp.databinding.ActivitySearchUserAcitivtyBinding
import com.example.chatapp.utils.FireBaseUtils
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchUserAcitivty : AppCompatActivity() {
    private lateinit var binding: ActivitySearchUserAcitivtyBinding
    private var adapter: SearchUserRecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchUserAcitivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.etUsernameSearch.requestFocus()


        binding.btnSearch.setOnClickListener{
            val searchTerm = binding.etUsernameSearch.text.toString()
            if (searchTerm.isEmpty() || searchTerm.length < 3) {
                binding.etUsernameSearch.error = "Invalid Username"
                return@setOnClickListener
            }
            setupSearchRecycleView(searchTerm)
        }
    }

    private fun setupSearchRecycleView(searchTerm: String) {
        val query =FireBaseUtils.allUserCollectionRefrence()
            .whereGreaterThanOrEqualTo("username", searchTerm)
            .whereLessThanOrEqualTo("username",searchTerm+'\uf8ff')

        val options = FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel::class.java).build()

        adapter = SearchUserRecyclerAdapter(options ,applicationContext)
        binding.recycleListAccount.layoutManager = LinearLayoutManager(this)
        binding.recycleListAccount.adapter = adapter
        adapter!!.startListening()
    }

    override fun onStart() {
        super.onStart()
        if(adapter!= null){
            adapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if(adapter!= null){
            adapter!!.stopListening()
        }
    }

    override fun onResume() {
        super.onResume()
        if (adapter != null) {
            adapter!!.startListening()
        }
    }
}