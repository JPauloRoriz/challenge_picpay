package com.picpay.desafio.android.presentation.users.viewModel.model

sealed class ListUsersEvent {
    data class ShowToasting(var message : String = "") : ListUsersEvent()
}