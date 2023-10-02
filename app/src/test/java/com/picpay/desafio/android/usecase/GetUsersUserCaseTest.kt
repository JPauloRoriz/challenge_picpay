//package com.picpay.desafio.android.usecase
//
//import com.picpay.desafio.android.data.repository.contract.UsersRepository
//import com.picpay.desafio.android.domain.model.UserModel
//import com.picpay.desafio.android.domain.usecase.GetUsersUseCaseImpl
//import com.picpay.desafio.android.domain.usecase.contract.GetUsersUseCase
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import org.mockito.Mock
//
//@RunWith(JUnit4::class)
//class GetUsersUserCaseTest {
//    @Mock
//    private lateinit var repository: UsersRepository
//    private lateinit var getUsersUseCase: GetUsersUseCase
//
//    @Before
//    fun setup() {
//        getUsersUseCase = GetUsersUseCaseImpl(repository)
//    }
//
//    @Test
//    fun `test getAllUsers`() = runBlocking {
//        // Crie um mock para o UsersRepository
//        val repository = mockk<UsersRepository>()
//
//        // Crie um objeto GetUsersUserCaseImpl com o mock do repository
//        val getUsersUseCase = GetUsersUseCaseImpl(repository)
//
//        // Defina um comportamento para o mock do repository quando getAllUsersModel() for chamado
//        val mockUserList = listOf(UserModel("", "", 2, ""))
//        coEvery { repository.getAllUsersModel() } returns flowOf(mockUserList)
//
//        // Chame a função que você deseja testar
//        val result = getUsersUseCase.getAllUsers().toList().first()
//
//        // Verifique se o resultado é o esperado
//        assertEquals(mockUserList, result)
//    }
//}