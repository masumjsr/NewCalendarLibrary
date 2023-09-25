package com.example.newcalendarlibrary.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newcalendarlibrary.room.user.User
import com.example.newcalendarlibrary.room.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(app.applicationContext)
    private var userExist = false
    val currentUser = MutableStateFlow<User?>(null)
    private val userFlow = currentUser.asStateFlow()

    suspend fun checkPassword(username: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = getUserByLoginAndPassword(username, password)
            user != null
        }
    }

    private fun setCurrentUser(user: User) {
        currentUser.value = user
    }

    suspend fun checkIfUserExists(loginValue: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = repo.getUserByLogin(loginValue)
            user != null
        }
    }
    private suspend fun getUserByLoginAndPassword(
        loginValue: String,
        passwordValue: String
    ): User? {
        return repo.getUserByLoginAndPassword(loginValue, passwordValue)
    }

    suspend fun login(loginValue: String, passwordValue: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = getUserByLoginAndPassword(loginValue, passwordValue)
            if (user != null) {
                setCurrentUser(user)
                userFlow.apply {
                    currentUser.asStateFlow()
                }
                println(currentUser.value)
                true
            } else {
                false
            }
        }
    }

    fun logout() {
        currentUser.value = null
    }


    fun registerUser(
        nameValue: String,
        loginValue: String,
        passwordValue: String,
        confirmPasswordValue: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (passwordValue == confirmPasswordValue) {
                userExist = false
                val user = User(
                    nameValue = nameValue,
                    loginValue = loginValue,
                    passwordValue = passwordValue
                )
                repo.insert(user)
            } else {
                userExist = true
            }
        }
    }
}