package com.picpay.desafio.android.presentation.users.viewModel.model

import com.picpay.desafio.android.R
import com.picpay.desafio.android.domain.model.UserModel

data class ListUsersState(
    val isLoading: Boolean = false,
    val showOffline: Boolean = false,
    val showError: Boolean = false,
    val iconError: Int = R.drawable.baseline_error_24,
    val titleError: String = "",
    val descriptionError :String = "",
    var usersModelList: List<UserModel> = listOf()
)