package com.picpay.desafio.android.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picpay.desafio.android.data.database.entity.DataUser

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
     fun getAllUsers(): List<DataUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertAll(users: List<DataUser>)
}
