package com.picpay.desafio.android.data.repository.contract

import com.picpay.desafio.android.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository  {
    fun getAllUsersModel() : Flow<Result<List<UserModel>>>
}