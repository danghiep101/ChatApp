package com.example.chatapp.views.messages

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.RecentChatRecyclerAdapter
import com.example.chatapp.data.model.ChatRoomModel
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.utils.FireBaseUtils
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query

class ChatFragment : Fragment() {

   private lateinit var binding: FragmentChatBinding
   private var adapter: RecentChatRecyclerAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(LayoutInflater.from(context), container, false)
        setupRecycleView()
        return binding.root

    }
    private fun setupRecycleView() {
        val query = FireBaseUtils.allChatRoomCollectionReference()
            .whereArrayContains("userIds", FireBaseUtils.currentUserId()as Any)
            .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)


        val options = FirestoreRecyclerOptions.Builder<ChatRoomModel>().setQuery(query, ChatRoomModel::class.java).build()

        adapter = RecentChatRecyclerAdapter(options ,requireContext())
       binding.recycleViewChat.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewChat.adapter = adapter
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        if (adapter != null) {
            adapter!!.notifyDataSetChanged()
        }
    }

}