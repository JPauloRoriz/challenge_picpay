package com.picpay.desafio.android.data.repository.mapper

import com.picpay.desafio.android.data.database.entity.DataUser
import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.domain.model.UserModel

fun List<UserResponse>.toUserModel(): MutableList<UserModel> {
    return map {
        UserModel(
            img = it.img.orEmpty(),
            name = it.name.orEmpty(),
            id = it.id,
            username = it.username.orEmpty()
        )
    }.toMutableList()

}

fun List<DataUser>.dataUserToUserModel(): List<UserModel> {
    return map {
        UserModel(
            name = it.name,
            img = it.img,
            id = it.id,
            username = it.username
        )
    }

}

fun List<UserResponse>.toDataUser(): MutableList<DataUser> {
    return map {
        DataUser(
            id = it.id?:0,
            img = it.img.orEmpty(),
            name = it.name.orEmpty(),
            username = it.username.orEmpty()
        )
    }.toMutableList()
}