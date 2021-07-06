package com.example.chatapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.adapters.RegisteredUserAdapter
import com.example.chatapplication.modelClasses.UserInfoModelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.chat_screen.*
import kotlinx.android.synthetic.main.registrered_users_fragment.*

class RegisteredUsersActivity : AppCompatActivity() {
    private var registerUserAdapter: RegisteredUserAdapter? = null
    private var registeredUsersList: List<UserInfoModelClass>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrered_users_fragment)
         searchEditText= findViewById(R.id.etSearch)
        recyclerView = findViewById(R.id.rvSearch)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext)
        registeredUsersList = ArrayList()
        rvSearch.layoutManager = LinearLayoutManager(this ,RecyclerView.VERTICAL, false)

        fethchAllUsers()
        searchEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(cs: CharSequence?, start: Int, before: Int, count: Int) {
                fetchingUsers(cs.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
    private fun fethchAllUsers() {
        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (registeredUsersList as ArrayList<UserInfoModelClass>).clear()
                for (snapShot in snapshot.children) {
                    val user: UserInfoModelClass? =
                        snapShot.getValue(UserInfoModelClass::class.java)
                    if (!(user!!.uid).equals(firebaseUserId)) {
                        (registeredUsersList as ArrayList<UserInfoModelClass>).add(user)
                    }
                }
                registerUserAdapter = RegisteredUserAdapter(applicationContext!!, registeredUsersList!!, false)
                recyclerView!!.adapter = registerUserAdapter
            }


            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun fetchingUsers(str: String) {
        var firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val queryUsers =
            FirebaseDatabase.getInstance().reference.child("Users").orderByChild("search")
                .startAt(str)
                .endAt(str + "\uf8ff")
        queryUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (registeredUsersList as ArrayList<UserInfoModelClass>).clear()
                if (etSearch!!.text.toString() == "") {
                    for (snapShot in snapshot.children) {
                        val user: UserInfoModelClass? =
                            snapShot.getValue(UserInfoModelClass::class.java)
                        if (!(user!!.uid).equals(firebaseUserID)) {
                            (registeredUsersList as ArrayList<UserInfoModelClass>).add(user)
                        }
                    }
                    registerUserAdapter =
                        RegisteredUserAdapter(applicationContext!!, registeredUsersList!!, false)
                    recyclerView!!.adapter = registerUserAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}