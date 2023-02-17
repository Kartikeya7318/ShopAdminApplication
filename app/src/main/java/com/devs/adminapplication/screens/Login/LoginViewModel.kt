package com.devs.adminapplication.screens.Login

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.MainActivity
import com.devs.adminapplication.SessionManager
import com.devs.adminapplication.models.LoginRequest
import com.devs.adminapplication.models.LoginResponse
import com.devs.adminapplication.repository.UserRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {


    val userRepo = UserRepository()
    val loginResult: MutableLiveData<LoginResponse> = MutableLiveData()
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason

    fun loginUser(userId: String, password: String ) {

        _loading.value=true
        viewModelScope.launch {
            try {

                val loginRequest = LoginRequest(
                    userId = userId,
                    password =password
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = response.body()
                    val applicationContext = getApplication<Application>()
                    SessionManager.saveAuthToken(applicationContext, loginResult.value?.token.toString())
                    _loading.value=false
                    Log.d("LoginFlow", "loginUser: "+SessionManager.getToken(applicationContext))
                    Toast.makeText(applicationContext,loginResult.value?.token.toString(),Toast.LENGTH_SHORT).show()
                } else {
                    if (response != null) {
                        _failReason.value=response.message()
                    }
                    _loading.value=false
                }


            } catch (ex: Exception) {
                _failReason.value = ex.message.toString()
            }
        }
    }
    fun resetFailReason(){
        _failReason.value=" "
    }
}