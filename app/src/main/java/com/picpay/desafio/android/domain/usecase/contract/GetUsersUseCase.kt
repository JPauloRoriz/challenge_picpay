package com.picpay.desafio.android.domain.usecase.contract

import com.picpay.desafio.android.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    suspend fun getAllUsers() : Flow<List<UserModel>>
}