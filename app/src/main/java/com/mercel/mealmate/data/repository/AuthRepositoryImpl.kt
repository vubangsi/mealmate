package com.mercel.mealmate.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.mercel.mealmate.data.local.dao.UserDao
import com.mercel.mealmate.data.local.entity.UserEntity
import com.mercel.mealmate.data.mapper.UserMapper
import com.mercel.mealmate.domain.model.AuthState
import com.mercel.mealmate.domain.model.User
import com.mercel.mealmate.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: DataStore<Preferences>,
    private val userMapper: UserMapper
) : AuthRepository {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_ID = stringPreferencesKey("user_id")
        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val LAST_LOGIN_TIME = longPreferencesKey("last_login_time")
    }

    override fun getAuthState(): Flow<AuthState> {
        return combine(
            dataStore.data.map { it[IS_LOGGED_IN] ?: false },
            dataStore.data.map { it[USER_ID] },
            dataStore.data.map { it[REMEMBER_ME] ?: false },
            dataStore.data.map { it[LAST_LOGIN_TIME] ?: 0L },
            userDao.getCurrentUser()
        ) { isLoggedIn, userId, rememberMe, lastLoginTime, userEntity ->
            AuthState(
                isLoggedIn = isLoggedIn && userEntity != null,
                user = userEntity?.let { userMapper.toDomain(it) },
                rememberMe = rememberMe,
                lastLoginTime = lastLoginTime
            )
        }
    }

    override suspend fun login(email: String, password: String, rememberMe: Boolean): Result<User> {
        return try {
            // Simple mock authentication - in real app, you'd validate against backend
            val existingUser = userDao.getUserByEmail(email)
            val user = if (existingUser != null) {
                userMapper.toDomain(existingUser)
            } else {
                // Create new user for demo purposes
                val newUser = User(
                    id = UUID.randomUUID().toString(),
                    email = email,
                    name = email.substringBefore('@').replaceFirstChar { it.uppercase() }
                )
                userDao.insertUser(userMapper.toEntity(newUser))
                newUser
            }

            // Update DataStore
            dataStore.edit { preferences ->
                preferences[IS_LOGGED_IN] = true
                preferences[USER_ID] = user.id
                preferences[REMEMBER_ME] = rememberMe
                preferences[LAST_LOGIN_TIME] = System.currentTimeMillis()
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, name: String): Result<User> {
        return try {
            // Check if user already exists
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("User already exists"))
            }

            val user = User(
                id = UUID.randomUUID().toString(),
                email = email,
                name = name
            )

            userDao.insertUser(userMapper.toEntity(user))

            // Auto-login after registration
            dataStore.edit { preferences ->
                preferences[IS_LOGGED_IN] = true
                preferences[USER_ID] = user.id
                preferences[REMEMBER_ME] = true
                preferences[LAST_LOGIN_TIME] = System.currentTimeMillis()
            }

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USER_ID] = ""
            preferences[LAST_LOGIN_TIME] = 0L
            // Keep REMEMBER_ME setting
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return dataStore.data.map { it[IS_LOGGED_IN] ?: false }.first()
    }

    override suspend fun getCurrentUser(): User? {
        val userId = dataStore.data.map { it[USER_ID] }.first()
        return userId?.let { id ->
            userDao.getUserById(id)?.let { userMapper.toDomain(it) }
        }
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(userMapper.toEntity(user))
    }
}