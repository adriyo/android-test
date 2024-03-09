@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalCoroutinesApi::class)

package com.adriyo.frontendtest.feature.login

import app.cash.turbine.test
import com.adriyo.frontendtest.MainDispatcherRule
import com.adriyo.frontendtest.TestDispatcherProvider
import com.adriyo.frontendtest.data.db.dao.UserDao
import com.adriyo.frontendtest.data.db.entity.UserEntity
import com.adriyo.frontendtest.data.db.mapper.EntityMapper
import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.data.model.UserRole
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.shared.Resource
import com.adriyo.frontendtest.shared.Validator
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

/**
 * Created by adriyo on 06/03/2024.
 * [Github](https://github.com/adriyo)
 */
class LoginTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testDispatchers = TestDispatcherProvider()

    private val resource = mockk<Resource>()
    private val userDao = mockk<UserDao>()
    private val entityMapper = mockk<EntityMapper>()
    private lateinit var validator: Validator
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        validator = Validator(resource)
        authRepository = AuthRepository(
            userDao = userDao,
            resource = resource,
            entityMapper = entityMapper
        )
        viewModel = LoginViewModel(
            authRepository = authRepository,
            validator = validator,
            dispatcher = testDispatchers,
        )
    }

    @Test
    fun `validate invalid form`() = runTest {
        val errorEmail = "invalid email"
        val errorPassword = "invalid password"

        every { resource.getString(2131427352) } returns errorEmail
        every { resource.getString(2131427353) } returns errorPassword
        viewModel.formEvent(FormEvent.OnLoginSubmit)

        val state = viewModel.uiState.value
        assertThat(state.emailError).isEqualTo(errorEmail)
        assertThat(state.passwordError).isEqualTo(errorPassword)
    }

    @Test
    fun `catch invalid account`() = runTest {
        val email = "email@mail.com"
        val password = "123456"
        val errorMessage = "error message"
        val resultFailure = Result.failure<User>(Exception(errorMessage))
        every { resource.getString(2131427359) } returns errorMessage
        coEvery { userDao.getInfo(email, password) } returns null
        coEvery { authRepository.login(email, password) } returns resultFailure

        viewModel.formEvent(FormEvent.OnEmailChange(email))
        viewModel.formEvent(FormEvent.OnPasswordChange(password))
        viewModel.formEvent(FormEvent.OnLoginSubmit)

        viewModel.loginEvent.test {
            assertThat((awaitItem() as AppEvent.ShowToast).error?.message).isEqualTo(resultFailure.exceptionOrNull()?.message)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `success login as admin, navigate to users screen`() = runTest {
        val email = "email@mail.com"
        val password = "123456"
        val userEntity =
            UserEntity(id = 1, username = "username", email = email, password = password, role = UserRole.ADMIN)
        every { resource.getString(2131427359) } returns ""
        coEvery { userDao.updateLoggedInStatus(email) } returns Unit
        coEvery { userDao.getInfo(email, password) } returns userEntity
        every { entityMapper.getUser(userEntity) } returns User(
            id = userEntity.id,
            username = userEntity.username,
            email = userEntity.email,
            role = userEntity.role,
            password = userEntity.password
        )

        viewModel.formEvent(FormEvent.OnEmailChange(email))
        viewModel.formEvent(FormEvent.OnPasswordChange(password))

        viewModel.formEvent(FormEvent.OnLoginSubmit)

        assertThat(validator.isEmailValid(email)).isEqualTo(Pair(true, null))
        assertThat(validator.isPasswordValid(password)).isEqualTo(Pair(true, null))

        coVerify { userDao.updateLoggedInStatus(email) }
        coVerify { entityMapper.getUser(userEntity) }

        viewModel.loginEvent.test {
            assertThat(awaitItem()).isEqualTo(AppEvent.NavigateToUserScreen)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `success login as regular, navigate to photos screen`() = runTest {
        val email = "email@mail.com"
        val password = "123456"
        val userEntity =
            UserEntity(id = 1, username = "username", email = email, password = password, role = UserRole.REGULAR)
        every { resource.getString(2131427359) } returns ""
        coEvery { userDao.updateLoggedInStatus(email) } returns Unit
        coEvery { userDao.getInfo(email, password) } returns userEntity
        every { entityMapper.getUser(userEntity) } returns User(
            id = userEntity.id,
            username = userEntity.username,
            email = userEntity.email,
            role = userEntity.role,
            password = userEntity.password
        )

        viewModel.formEvent(FormEvent.OnEmailChange(email))
        viewModel.formEvent(FormEvent.OnPasswordChange(password))

        viewModel.formEvent(FormEvent.OnLoginSubmit)

        assertThat(validator.isEmailValid(email)).isEqualTo(Pair(true, null))
        assertThat(validator.isPasswordValid(password)).isEqualTo(Pair(true, null))

        coVerify { userDao.updateLoggedInStatus(email) }
        coVerify { entityMapper.getUser(userEntity) }

        viewModel.loginEvent.test {
            assertThat(awaitItem()).isEqualTo(AppEvent.NavigateToPhotoScreen)
            cancelAndConsumeRemainingEvents()
        }
    }

}