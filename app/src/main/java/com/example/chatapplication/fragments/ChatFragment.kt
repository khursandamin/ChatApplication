package com.example.chatapplication.fragments

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.R
import com.example.chatapplication.modelClasses.UserInfoModelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_fragment.*

class ChatFragment() : Fragment(R.layout.chat_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var refUsers: DatabaseReference? = null
        var firebaseUSer: FirebaseUser? = null
        super.onViewCreated(view, savedInstanceState)

//        if(activity is AppCompatActivity){
//            (activity as AppCompatActivity).setSupportActionBar(tbToolBarMain)
//        }

        firebaseUSer = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUSer!!.uid)
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user: UserInfoModelClass? =
                        snapshot.getValue(UserInfoModelClass::class.java)
                    tvUsernameChatScreen.text = user!!.username
                    Picasso.get().load(user.profile).placeholder(R.drawable.profile)
                        .into(ivProfileImageChat)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_menu -> {
                FirebaseAuth.getInstance().signOut()
                val action = ChatFragmentDirections.actionChatFragmentToWelcomeFragment()
                findNavController().navigate(action)
                return true
            }

        }
        return false
    }
}
