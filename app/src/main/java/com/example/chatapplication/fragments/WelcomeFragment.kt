package com.example.chatapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.welcome_fragment.*

class WelcomeFragment:Fragment(R.layout.welcome_fragment) {
    var firebaseUser: FirebaseUser? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseApp.initializeApp(requireContext())

        btnLoginPage.setOnClickListener {
            val action=WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        btnRegisterPage.setOnClickListener {
            val action=WelcomeFragmentDirections.actionWelcomeFragmentToRegistrationFragments()
            findNavController().navigate(action)
        }

    }
    override fun onStart() {
        super.onStart()

        firebaseUser=FirebaseAuth.getInstance().currentUser


        if (firebaseUser!=null){

            val action=WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            findNavController().navigate(action)


        }
    }
}

