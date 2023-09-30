package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.database.UserDao
import com.picpay.desafio.android.data.repository.contract.UsersRepository
import com.picpay.desafio.android.data.repository.exception.ConnectionException
import com.picpay.desafio.android.data.repository.exception.DefaultException
import com.picpay.desafio.android.data.repository.mapper.dataUserToUserModel
import com.picpay.desafio.android.data.repository.mapper.toDataUser
import com.picpay.desafio.android.data.repository.mapper.toUserModel
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class UsersRepositoryImpl(
    private val service: PicPayService,
    private val userDatabase: UserDao
) : UsersRepository {

    override suspend fun getAllUsersModel(): Flow<List<UserModel>> = flow {
        val localData = userDatabase.getAllUsers()
        emit(localData.dataUserToUserModel())
        runCatching { service.getUsers() }
            .onSuccess { remoteData ->
                userDatabase.insertAll(remoteData.toDataUser())
                emit(remoteData.toUserModel())
            }.onFailure { error ->
                when (error) {
                    is IOException -> {
                        throw ConnectionException()
                    }
                    else -> {
                        throw DefaultException()
                    }
                }
            }
    }
}