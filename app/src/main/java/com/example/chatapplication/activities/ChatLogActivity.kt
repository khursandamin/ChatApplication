package com.example.chatapplication.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.R
import com.example.chatapplication.modelClasses.UserInfoModelClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.item_left_chat.view.*
import kotlinx.android.synthetic.main.item_right_chat.view.*

class ChatLogActivity : AppCompatActivity() {
    val adapter = GroupAdapter<ViewHolder>()
    var toUser:UserInfoModelClass?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        recyclerviewChatLog.adapter = adapter
        recyclerviewChatLog.layoutManager = LinearLayoutManager(applicationContext)

       toUser = intent.getParcelableExtra<UserInfoModelClass>("idKey")
        supportActionBar?.title = toUser!!.username
        btnSendChatLog.setOnClickListener {
            performMessage()
            listenForMessages()
        }

    }

    private fun listenForMessages() {
        val fromId=FirebaseAuth.getInstance().uid
        val toId=toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.e("checkCmessage", "message" + chatMessage.text)
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))

                    } else {
                        adapter.add(ChatToItem(chatMessage.text))

                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }




    private fun performMessage() {
        val text = etEnterMessage.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<UserInfoModelClass>("idKey")
        val toId = user!!.uid
        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage =
            ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
        etEnterMessage.text.clear()
        recyclerviewChatLog.scrollToPosition(adapter.itemCount -1)
        toReference.setValue(chatMessage)
    }
}

class ChatFromItem(val text: String) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvUsernameRight.text = text
    }

    override fun getLayout(): Int {
        return R.layout.item_right_chat
    }

}
    class ChatToItem(val text: String) : Item<ViewHolder>() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.tvUsernameLeft.text = text

        }

        override fun getLayout(): Int {
            return R.layout.item_left_chat
        }

    }

class ChatMessage(
    val id: String,
    val text: String,
    val fromId: String,
    val toId: String,
    val timespan: Long
) {
    constructor() : this("", "", "", "", -1)
}