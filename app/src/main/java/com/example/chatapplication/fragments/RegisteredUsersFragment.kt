package com.example.chatapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.registrered_users_fragment.*

class RegisteredUsersFragment : Fragment() {
    private var registerUserAdapter: RegisteredUserAdapter? = null
    private var registeredUsersList: List<UserInfoModelClass>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.registrered_users_fragment, container, false)
        searchEditText=view.findViewById(R.id.etSearch)
        recyclerView = view.findViewById(R.id.rvSearch)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        registeredUsersList = ArrayList()
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

        return view
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
                registerUserAdapter = RegisteredUserAdapter(context!!, registeredUsersList!!, false)
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
                        RegisteredUserAdapter(context!!, registeredUsersList!!, false)
                    recyclerView!!.adapter = registerUserAdapter

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}