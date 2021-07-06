package com.example.chatapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatapplication.R
import com.example.chatapplication.modelClasses.UserInfoModelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UpdateActivity : AppCompatActivity() {
 private lateinit var DISLAY_NAME:String
 private lateinit var PROFILE_IMAGE_URL:String
val firebaseUser:FirebaseUser? = null
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
val user  = intent.getParcelableExtra<UserInfoModelClass>("idKey")
         if (user!=null){
            Log.e("checkprofileUser","updateUser"+user.uid)
        }
    }
}