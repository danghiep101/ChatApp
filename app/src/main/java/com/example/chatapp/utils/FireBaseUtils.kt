package com.example.chatapp.utils


import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

object FireBaseUtils {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    fun currentUserId(): String?{
        return firebaseAuth.uid
    }

    fun currentUserDetails(): DocumentReference{
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId()!!)
    }

    fun allUserCollectionRefrence(): CollectionReference{
        return FirebaseFirestore.getInstance().collection("users")
    }

    fun getChatRoomReference(chatRoomId: String): DocumentReference{
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId)
    }

    fun getChatRomMessageRefrence(chatRoomId: String): CollectionReference{
        return getChatRoomReference(chatRoomId).collection("chats")
    }
    fun getChatRoomId(userId1: String, userId2: String): String{
        return if(userId1.hashCode() < userId2.hashCode()){
            userId1+"_"+userId2
        }else{
            userId2+"_"+userId1
        }
    }

    fun allChatRoomCollectionReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("chatrooms")
    }

    fun getOtherUserFromChatRoom(userIds: List<String> ): DocumentReference{
        return if(userIds[0] == currentUserId()){
            allUserCollectionRefrence().document(userIds[1])
        }else{
            allUserCollectionRefrence().document(userIds[0])
        }
    }


    fun timestampToString(timestamp: Timestamp): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(timestamp.toDate())
    }
}