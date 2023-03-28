package com.mvvm.appcomponent.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id :Int?=null,
    val userId :Int,
    val name : String,
    val email: String,
    val gender:String,
    var username: String? = ""
)