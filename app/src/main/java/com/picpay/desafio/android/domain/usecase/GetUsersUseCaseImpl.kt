package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.data.repository.contract.UsersRepository
import com.picpay.desafio.android.domain.model.UserModel
import com.picpay.desafio.android.domain.usecase.contract.GetUsersUseCase
import kotlinx.coroutines.flow.Flow

class GetUsersUseCaseImpl(
    private val repository: UsersRepository
) : GetUsersUseCase {
    override suspend fun getAllUsers(): Flow<List<UserModel>> {
        return repository.getAllUsersModel()
    }
}