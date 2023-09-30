package com.picpay.desafio.android.viewModel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picpay.desafio.android.R
import com.picpay.desafio.android.domain.model.UserModel
import com.picpay.desafio.android.domain.usecase.contract.GetUsersUseCase
import com.picpay.desafio.android.presentation.users.viewModel.UsersViewModel
import com.picpay.desafio.android.presentation.users.viewModel.model.ListUsersState
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: UsersViewModel
    private val useCase: GetUsersUseCase = mockk()
    private val application: Application = mockk()
    private var stateObserver: Observer<ListUsersState> = mockk(relaxed = true)

    private fun setupBefore() {
        viewModel = spyk(UsersViewModel(useCase, application), recordPrivateCalls = true)
        viewModel.stateLiveData.observeForever(stateObserver)
    }

    @Test
    fun `when call refresh with success return list of users`() = runBlocking {
        setupBefore()
        // Given
        val usersModelMock = listOf(UserModel("img1", "name1", 1, "username1"))

        // When
        coEvery { useCase.getAllUsers() } returns flowOf(usersModelMock)
//        coEvery { viewModel.treatmentSuccess(usersModelMock) } returns stateObserver.onChanged()

        // Then
        verifySequence {
            verifySequence {
                stateObserver.onChanged(
                    ListUsersState(
                        isLoading = true,
                        showOffline = false,
                        usersModelList = emptyList(),
                        showError = false,
                        iconError = R.drawable.baseline_error_24,
                        titleError = "",
                        descriptionError = ""
                    )
                )

                stateObserver.onChanged(
                    ListUsersState(
                        isLoading = false,
                        showOffline = false,
                        usersModelList = usersModelMock,
                        showError = false,
                        iconError = R.drawable.baseline_error_24,
                        titleError = "",
                        descriptionError = ""
                    )
                )
            }
        }

    }
}