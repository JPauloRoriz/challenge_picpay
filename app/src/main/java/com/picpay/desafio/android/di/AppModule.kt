package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.database.AppDatabase
import com.picpay.desafio.android.data.repository.UsersRepositoryImpl
import com.picpay.desafio.android.data.repository.contract.UsersRepository
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.data.service.settings.retrofit
import com.picpay.desafio.android.domain.usecase.GetUsersUseCaseImpl
import com.picpay.desafio.android.domain.usecase.contract.GetUsersUseCase
import com.picpay.desafio.android.presentation.users.viewModel.UsersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    viewModel { UsersViewModel(get(), get()) }

    factory<GetUsersUseCase> { GetUsersUseCaseImpl(get()) }

    factory<UsersRepository> { UsersRepositoryImpl(get(), get()) }

    single { retrofit }

    single { get<Retrofit>().create(PicPayService::class.java) }

    single { AppDatabase.getInstance(get()) }

    single { get<AppDatabase>().userDao() }
}