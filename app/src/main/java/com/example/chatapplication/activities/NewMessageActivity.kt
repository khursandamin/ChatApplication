package com.example.chatapplication.activities

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
 import com.example.chatapplication.R
 import com.example.chatapplication.modelClasses.UserInfoModelClass
 import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
 import kotlinx.android.synthetic.main.registered_users_item.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title="selectUser"

        fetchUsers()
    }

    private fun fetchUsers() {

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(UserInfoModelClass::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }

                }

                adapter.setOnItemClickListener { item, view ->
                    val userItem= item as UserItem
                    val intent= Intent(view.context,ChatLogActivity::class.java)
                    intent.putExtra("idKey",userItem.user )
                    startActivity(intent)
                    finish()
                }
                recyclerviewNewMessage.adapter = adapter
                recyclerviewNewMessage.layoutManager= LinearLayoutManager(applicationContext)


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: UserInfoModelClass): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvUsernameNewMessage.text = user.username

        Picasso.get().load(user.profile).into(viewHolder.itemView.ivCircleImageViewNewMessage)
    }

    override fun getLayout(): Int {
        return R.layout.registered_users_item
    }
}