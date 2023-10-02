package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.database.UserDao
import com.picpay.desafio.android.data.repository.contract.UsersRepository
import com.picpay.desafio.android.data.repository.mapper.dataUserToUserModel
import com.picpay.desafio.android.data.repository.mapper.toDataUser
import com.picpay.desafio.android.data.repository.mapper.toUserModel
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.domain.exception.ConnectionException
import com.picpay.desafio.android.domain.exception.DefaultException
import com.picpay.desafio.android.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class UsersRepositoryImpl(
    private val service: PicPayService,
    private val userDatabase: UserDao
) : UsersRepository {

    override fun getAllUsersModel(): Flow<Result<List<UserModel>>> = flow {
        val localData = userDatabase.getAllUsers()
        emit(Result.success(localData.dataUserToUserModel()))
        runCatching { service.getUsers() }
            .onSuccess { remoteData ->
                userDatabase.insertAll(remoteData.toDataUser())
                emit(Result.success(remoteData.toUserModel()))
            }.onFailure { error ->
                when (error) {
                    is IOException -> {
                        emit(Result.failure(ConnectionException()))
                    }
                    else -> {
                        emit(Result.failure(DefaultException()))
                    }
                }
            }
    }
}