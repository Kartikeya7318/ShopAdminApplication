package com.devs.adminapplication.screens.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.MyPreference
import com.devs.adminapplication.models.LoginRequest
import com.devs.adminapplication.models.LoginResponse
import com.devs.adminapplication.navigation.AdminNavigation
import com.devs.adminapplication.navigation.AdminScreens
import com.devs.adminapplication.repository.UserRepository
import com.devs.adminapplication.retrofit.Constants
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
                    password = password
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = response.body()
//                    val applicationContext = getApplication<Application>().applicationContext
//                    SessionManager.saveAuthToken(applicationContext, loginResult.value?.token.toString())
                    _loading.value = false

//                    val editor = sharedpreferences.edit()
//                    editor.putString(Constants.USER_TOKEN,loginResult.value?.token.toString() )
//                    editor.apply()
                    _token.value = loginResult.value?.token.toString()
                    Log.d("LoginFlow", "loginUser: " + _token.value)
                    myPreferences.setStoredTag(_token.value)
                    Log.d("LoginFlow", "loginUser: saved " + myPreferences.getStoredTag())
                    home()
//                    Toast.makeText(
//                        applicationContext,
//                        loginResult.value?.token.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()

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