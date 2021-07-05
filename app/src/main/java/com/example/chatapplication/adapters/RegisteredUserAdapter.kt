package com.example.chatapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.modelClasses.UserInfoModelClass
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.chat_fragment.*

class RegisteredUserAdapter(
    registeredUserContext: Context,
    registeredUsers: List<UserInfoModelClass>,
    isChatCheck: Boolean
) : RecyclerView.Adapter<RegisteredUserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsers: List<UserInfoModelClass>
    private val isChatCheck: Boolean

    init {
        this.mUsers = registeredUsers
        this.isChatCheck = isChatCheck
        this.mContext = registeredUserContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.registered_users_item, parent, false)
        return RegisteredUserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user: UserInfoModelClass = mUsers[position]
        holder.userNameTxt.text = user!!.username


        Picasso . get ().load(user.profile).placeholder(R.drawable.profile)
                .into(holder.profileImageView)
    }

    override fun getItemCount(): Int {

        return mUsers.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userNameTxt: TextView
        var profileImageView: CircleImageView


        init {

            userNameTxt = itemView.findViewById(R.id.tvUsernameRegistered)
            profileImageView = itemView.findViewById(R.id.ivCircleImageView)

        }
    }


}