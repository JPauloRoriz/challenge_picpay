package com.picpay.desafio.android.viewModel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picpay.desafio.android.R
import com.picpay.desafio.android.domain.exception.ConnectionException
import com.picpay.desafio.android.domain.exception.DefaultException
import com.picpay.desafio.android.domain.model.UserModel
import com.picpay.desafio.android.domain.usecase.contract.GetUsersUseCase
import com.picpay.desafio.android.presentation.users.viewModel.UsersViewModel
import com.picpay.desafio.android.presentation.users.viewModel.model.ListUsersEvent
import com.picpay.desafio.android.presentation.users.viewModel.model.ListUsersState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import io.mockk.verifySequence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val useCase: GetUsersUseCase = mockk(relaxed = true)
    private val application: Application = mockk(relaxed = true)
    private var stateObserver: Observer<ListUsersState> = mockk(relaxed = true)
    private var eventObserver: Observer<ListUsersEvent> = mockk(relaxed = true)


    @Test
    fun `when call refresh with success return list of users`() {
        // Given
        val usersModelMock = listOf(UserModel("img1", "name1", 1, "username1"))
        coEvery { useCase.getAllUsers() } returns flowOf(Result.success(usersModelMock))

        // When
        val viewModel = UsersViewModel(useCase, application)
        viewModel.stateLiveData.observeForever(stateObserver)
        viewModel.eventLiveData.observeForever(eventObserver)

        // Then
        verifyOrder {
            stateObserver.onChanged(
                ListUsersState(
                    isLoading = true,
                )
            )

            stateObserver.onChanged(
                ListUsersState(
                    isLoading = false,
                    showOffline = false,
                    usersModelList = usersModelMock
                )
            )
        }
    }

    @Test
    fun `when call refresh with success return list of users and list of cache is empty`() {
        // Given
        val usersModelMock = mutableListOf<UserModel>()
        coEvery { useCase.getAllUsers() } returns flowOf(Result.success(usersModelMock))

        // When
        val viewModel = UsersViewModel(useCase, application)
        viewModel.stateLiveData.observeForever(stateObserver)
        viewModel.eventLiveData.observeForever(eventObserver)

        // Then
        verifyOrder {
            stateObserver.onChanged(
                ListUsersState(
                    isLoading = true,
                )
            )
        }
    }

    @Test
    fun `when call refresh with error and usersModelList is empty return error of connection`() {
        // Given
        val expectedException = ConnectionException()
        val expectedTitle = "title"
        val expectedMessage = "message"
        coEvery { useCase.getAllUsers() } returns flowOf(Result.failure(expectedException))
        coEvery { application.getString(R.string.attention) } returns expectedTitle
        coEvery { application.getString(R.string.error_description_connection) } returns expectedMessage

        // When
        val viewModel = UsersViewModel(useCase, application)
        viewModel.stateLiveData.observeForever(stateObserver)
        viewModel.eventLiveData.observeForever(eventObserver)

        // Then
        verifySequence {
            stateObserver.onChanged(
                ListUsersState(
                    isLoading = true,
                )
            )

            stateObserver.onChanged(
                ListUsersState(
                    showOffline = false,
                    isLoading = false,
                    showError = true,
                    iconError = R.drawable.baseline_error_24,
                    titleError = expectedTitle,
                    descriptionError = expectedMessage
                )
            )
        }
    }

    @Test
    fun `when call refresh with error and usersModelList is empty return error default`() {
        // Given
        val expectedException = DefaultException()
        val expectedTitle = "title"
        val expectedMessage = "message"
        coEvery { useCase.getAllUsers() } returns flowOf(Result.failure(expectedException))
        coEvery { application.getString(R.string.attention) } returns expectedTitle
        coEvery { application.getString(R.string.default_exception) } returns expectedMessage

        // When
        val viewModel = UsersViewModel(useCase, application)
        viewModel.stateLiveData.observeForever(stateObserver)
        viewModel.eventLiveData.observeForever(eventObserver)

        // Then
        verifySequence {
            stateObserver.onChanged(
                ListUsersState(
                    isLoading = true,
                )
            )

            stateObserver.onChanged(
                ListUsersState(
                    showOffline = false,
                    isLoading = false,
                    showError = true,
                    iconError = R.drawable.baseline_extension_off_24,
                    titleError = expectedTitle,
                    descriptionError = expectedMessage
                )
            )
        }
    }

    @Test
    fun `when call refresh with error and usersModelList not is empty return error`() {
        // Given
        val usersModelMock = listOf(UserModel("img1", "name1", 1, "username1"))
        val expectedException = Exception()
        val expectedTitle = "title"
        val expectedMessage = "message"
        coEvery { useCase.getAllUsers() } returns flowOf(Result.failure(expectedException))
        coEvery { application.getString(R.string.attention) } returns expectedTitle
        coEvery { application.getString(R.string.error_description_connection) } returns expectedMessage

        // When
        val viewModel = UsersViewModel(useCase, application)
        viewModel.stateLiveData.value?.usersModelList = usersModelMock
        viewModel.stateLiveData.observeForever(stateObserver)
        viewModel.eventLiveData.observeForever(eventObserver)

        // Then
        verifySequence {

            stateObserver.onChanged(
                ListUsersState(
                    usersModelList = usersModelMock,
                    isLoading = true,
                )
            )

            stateObserver.onChanged(
                ListUsersState(
                    usersModelList = usersModelMock,
                    showOffline = true,
                    showError = false,
                    isLoading = false,
                )
            )
        }
    }
}