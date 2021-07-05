package com.example.chatapplication.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.registation_fragment.*

class LoginFragment:Fragment(R.layout.login_fragment) {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email:String=etEmailLogin.text.toString()
        val  password:String=etPasswordLogin.text.toString()
        if (email ==""){
            Toast.makeText(requireContext(),"please enter the email", Toast.LENGTH_SHORT).show()

        }
        else  if (password ==""){
            Toast.makeText(requireContext(),"please enter the password", Toast.LENGTH_SHORT).show()

        }else
        {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                task-> if (task.isSuccessful)
            {
                  val action=LoginFragmentDirections.actionLoginFragmentToRegisteredUsersFragment()
                findNavController().navigate(action)

            }else
                {
                    Toast.makeText(requireContext(),"Error Message:"+task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()


                }
            }

        }
     }
}