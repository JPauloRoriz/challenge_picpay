package com.picpay.desafio.android

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.data.database.UserDao
import com.picpay.desafio.android.data.database.entity.DataUser
import com.picpay.desafio.android.data.model.UserResponse
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.presentation.users.UsersActivity
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import java.io.IOException


class UsersActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val picPayService: PicPayService = mockk(relaxed = true)
    private val userDao: UserDao = mockk(relaxed = true)
    private val module by lazy {
        module {
            single { userDao }
            single { picPayService }
        }
    }

    @Before
    fun setupBefore() {
        loadKoinModules(module)
    }

    @After
    fun setupAfter() {
        unloadKoinModules(module)
    }

    @Test
    fun shouldDisplayListItem() {

        launchActivity<UsersActivity>().apply {
            val expectedTitle = context.getString(R.string.title)

            moveToState(Lifecycle.State.RESUMED)

            onView(withText(expectedTitle)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun verifyListSuccessWithoutCache() {
        coEvery { userDao.getAllUsers() } returns successCache
        coEvery { picPayService.getUsers() } returns successResponse

        launchActivity<UsersActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
            onView(withText("João")).check(matches(isDisplayed()))
            onView(withText("teste")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun verifyListErrorWithCache() {
        coEvery { userDao.getAllUsers() } returns successCache
        coEvery { picPayService.getUsers() } throws Exception()

        launchActivity<UsersActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
            onView(withText("teste2")).check(matches(isDisplayed()))
            onView(withText("Pedro")).check(matches(isDisplayed()))
            onView(withText(R.string.text_offline)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun verifyListErrorConnectionWithoutCache() {
        coEvery { userDao.getAllUsers() } returns listOf()
        coEvery { picPayService.getUsers() } throws IOException()

        launchActivity<UsersActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
            onView(withText(R.string.attention)).check(matches(isDisplayed()))
            onView(withText(R.string.default_exception)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun verifyListErrorWithoutCache() {
        coEvery { userDao.getAllUsers() } returns listOf()
        coEvery { picPayService.getUsers() } throws IOException()

        launchActivity<UsersActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
            onView(withText(R.string.attention)).check(matches(isDisplayed()))
            onView(withText(R.string.default_exception)).check(matches(isDisplayed()))
        }
    }

    companion object {

        private val successResponse by lazy {
            listOf(UserResponse("img", "João", 15, "teste"))
        }
        private val successCache by lazy {
            listOf(DataUser(6, "img", "Pedro", "teste2"))
        }
    }
}