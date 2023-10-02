package com.picpay.desafio.android.domain.usecase.contract

import com.picpay.desafio.android.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface GetUsersUseCase {
    fun getAllUsers() : Flow<Result<List<UserModel>>>
}