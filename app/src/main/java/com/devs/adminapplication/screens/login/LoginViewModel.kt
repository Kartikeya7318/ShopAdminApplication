package com.devs.adminapplication.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.utils.MyPreference
import com.devs.adminapplication.models.login.LoginRequest
import com.devs.adminapplication.models.login.LoginResponse
import com.devs.adminapplication.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val myPreferences: MyPreference
) :
    ViewModel() {


    private val loginResult: MutableLiveData<LoginResponse> = MutableLiveData()
    private val _token = MutableStateFlow(" ")
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason

    fun loginUser(userId: String, password: String,home: () -> Unit) {

        _loading.value = true
        viewModelScope.launch {
            try {

                val loginRequest = LoginRequest(
                    userId = userId,
                    password = password,
                    role="Admin"
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = response.body()
                    _loading.value = false
                    _token.value = loginResult.value?.token.toString()
                    Log.d("LoginFlow", "loginUser: " + _token.value)
                    myPreferences.setStoredTag("Bearer "+_token.value)
                    Log.d("LoginFlow", "loginUser: saved " + myPreferences.getStoredTag())
                    home()
                } else {
                    if (response != null) {
                        _failReason.value = response.message()
                        Log.d("LoginFlow", "failreason: " + _failReason.value.toString())
                    }
                    _loading.value = false
                }


            } catch (ex: Exception) {
                _failReason.value = ex.message.toString()
                Log.d("LoginFlow", "failreason: " + _failReason.value.toString())
            }
        }

    }

    fun resetFailReason() {
        _failReason.value = " "
    }
}