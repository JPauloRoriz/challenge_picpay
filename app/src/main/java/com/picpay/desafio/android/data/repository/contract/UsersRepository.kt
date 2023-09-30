package com.picpay.desafio.android.data.repository.contract

import com.picpay.desafio.android.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository  {
    suspend fun getAllUsersModel() : Flow<List<UserModel>>
}