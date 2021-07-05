package com.example.chatapplication.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.registation_fragment.*
import kotlin.concurrent.timerTask

class RegistrationFragments:Fragment (R.layout.registation_fragment){
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var userRef:DatabaseReference
    private var firebaseUserId:String=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()
        btnRegister.setOnClickListener {
            userRegistration()
        }
    }

    private fun userRegistration() {
        val username:String=etUsernameRegistration.text.toString().trim()
        val email:String=etEmailRegistration.text.toString().trim()
        val  password:String=etPasswordRegistration.text.toString().trim()
        if (username ==""){
Toast.makeText(requireContext(),"please enter the username",Toast.LENGTH_SHORT).show()
        }
        else  if (email ==""){
            Toast.makeText(requireContext(),"please enter the email",Toast.LENGTH_SHORT).show()

        }
        else  if (password ==""){
            Toast.makeText(requireContext(),"please enter the password",Toast.LENGTH_SHORT).show()

        }
        else{
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                . addOnCompleteListener{
                    task ->
                    if (task.isSuccessful)
                    {
                    firebaseUserId=firebaseAuth.currentUser!!.uid
                        userRef=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)
                        val userHashMap=HashMap<String,Any>()
                        userHashMap["uid"]=firebaseUserId
                        userHashMap["username"]=username
                        userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/chat-application-e6dac.appspot.com/o/profile.png?alt=media&token=3afd8936-6f64-442b-a5b8-5df342e65b4d"
                       userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/chat-application-e6dac.appspot.com/o/cover.jpg?alt=media&token=d0bfe370-8516-4ba2-8819-40afafdd40c6"
                        userHashMap["status"]="offline"
                        userHashMap["search"]=username.toLowerCase()
                        userHashMap["facebook"]="https://m.facebook.com"
                        userHashMap["instagram"]="https://m.instagram.com"
                        userHashMap["website"]="https://www.facebook.com"

                        userRef.updateChildren(userHashMap).addOnCompleteListener { task ->
                            if (task.isSuccessful){
                                val action=RegistrationFragmentsDirections.actionRegistrationFragmentsToLoginFragment()
                                    findNavController( ).navigate(action)

                            }

                        }

                    }else
                    {
                        Toast.makeText(requireContext(),"Error Message:"+task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()

                    }
                }
        }
     }

}

