import com.picpay.desafio.android.data.database.UserDao
import com.picpay.desafio.android.data.database.entity.DataUser
import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.data.repository.UsersRepositoryImpl
import com.picpay.desafio.android.data.repository.mapper.toUserModel
import com.picpay.desafio.android.data.service.PicPayService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UsersRepositoryImplTest {

    private lateinit var repository: UsersRepositoryImpl
    private val mockService: PicPayService = mockk()
    private val mockUserDao: UserDao = mockk()

    @Before
    fun setUp() {
        repository = UsersRepositoryImpl(mockService, mockUserDao)
    }

    @Test
    fun `when getAllUsersModel with remote and local data available`() = runBlocking {
        // Given
        val mockRemoteData = listOf(UserResponse("img1", "name1", 1, "username1"))
        val mockLocalData = listOf(DataUser(1, "img2", "name2", "username2"))

        coEvery { mockService.getUsers() } returns mockRemoteData

        coEvery { mockUserDao.getAllUsers() } returns mockLocalData

        // When
        val result = repository.getAllUsersModel().toList()

        // Then
        assertEquals(2, result.size)
        assertEquals(mockLocalData.toUserModel(), result[0])
        assertEquals(mockRemoteData.toUserModel(), result[1])
    }


}

