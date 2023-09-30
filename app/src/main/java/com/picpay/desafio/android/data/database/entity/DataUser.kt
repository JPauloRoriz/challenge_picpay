package com.picpay.desafio.android.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class DataUser(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(name = "img")
    var img: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "username")
    var username: String = ""
)