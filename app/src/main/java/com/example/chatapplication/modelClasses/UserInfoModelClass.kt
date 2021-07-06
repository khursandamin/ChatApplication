package com.example.chatapplication.modelClasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserInfoModelClass(
    var uid: String,
    var username: String,
    var profile: String, ) : Parcelable {
    constructor() : this("", "", "")


}