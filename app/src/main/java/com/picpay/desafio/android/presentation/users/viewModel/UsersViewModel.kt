package com.picpay.desafio.android.presentation.users.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.repository.exception.ConnectionException
import com.picpay.desafio.android.data.repository.exception.DefaultException
import com.picpay.desafio.android.domain.model.UserModel
import com.picpay.desafio.android.domain.usecase.contract.GetUsersUseCase
import com.picpay.desafio.android.presentation.commons.SingleLiveEvent
import com.picpay.desafio.android.presentation.users.viewModel.model.ListUsersEvent
import com.picpay.desafio.android.presentation.users.viewModel.model.ListUsersState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class UsersViewModel(
    private val useCase: GetUsersUseCase,
    private val application: Application
) : ViewModel() {

    private val _stateLiveData = MutableLiveData(ListUsersState())
    val stateLiveData: LiveData<ListUsersState> = _stateLiveData

    private val _eventLiveData = SingleLiveEvent<ListUsersEvent>()
    val eventLiveData: LiveData<ListUsersEvent> = _eventLiveData

    init {
        refresh()
    }

    private fun refresh() {
        _stateLiveData.postValue(ListUsersState(isLoading = true))
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getAllUsers()
                .catch { error -> treatmentError(error) }
                .collect { result -> treatmentSuccess(result) }
        }
    }

     fun treatmentSuccess(result: List<UserModel>) {
        _stateLiveData.postValue(
            _stateLiveData.value?.copy(
                isLoading = false,
                showOffline = false,
                usersModelList = result
            )
        )
    }


    private fun treatmentError(error: Throwable) {
        viewModelScope.launch(Dispatchers.Main) {
            _stateLiveData.value?.let { state ->
                if (state.usersModelList.isNotEmpty()) {
                    _stateLiveData.value = _stateLiveData.value?.copy(
                        showOffline = true,
                        showError = false,
                        isLoading = false,
                    )

                } else {
                    when (error) {
                        is ConnectionException -> {
                            _stateLiveData.value =
                                _stateLiveData.value?.copy(
                                    showOffline = false,
                                    isLoading = false,
                                    showError = true,
                                    iconError = R.drawable.baseline_error_24,
                                    titleError = application.getString(R.string.attention),
                                    descriptionError = application.getString(R.string.error_description_connection)
                                )
                        }
                        is DefaultException -> {

                        }
                    }
                }
            }
        }
    }
}