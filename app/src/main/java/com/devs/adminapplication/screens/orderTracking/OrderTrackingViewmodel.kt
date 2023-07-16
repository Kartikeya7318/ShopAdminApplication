package com.devs.adminapplication.screens.orderTracking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.utils.MyPreference
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

data class TrackingState(
    val orderId:Long,
    val trackingNumber:String="",
    val comapnyName:String="",
    val departureDt:String="",

)
data class UpdateTrackingResponse(
    val errorCode: String,
    val errorMessage: String,
)
@HiltViewModel
class OrderTrackingViewmodel @Inject constructor(
    private val myPreference: MyPreference,
    private val api: AdminShopApi,
) :ViewModel(){
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _failReason = MutableStateFlow(" ")
    val failReason: StateFlow<String> = _failReason
//    private val _trackingState = MutableStateFlow(TrackingState())
//    val trackingState get() = _trackingState.asStateFlow()

    fun updateTrackingDetail(detail: TrackingState){
        _loading.value=true
        val requestBody= Gson().toJson(detail).toRequestBody("application/json".toMediaTypeOrNull())
        viewModelScope.launch {
            try {
                val response=api.updateTrackingDetail(
                    token = myPreference.getStoredTag(),
                    requestBody = requestBody
                )
                if (response.errorCode.isNotEmpty()){
                    _loading.value=false
                    if (response.errorCode=="000000"){
                        _failReason.value=response.errorMessage
                    }else{
                        _failReason.value=response.errorMessage
                    }
                }
            }catch (ex: Exception) {
                _loading.value=false
                _failReason.value=ex.message.toString()
                Log.d("OrderTracking", "failreason: " +ex.message.toString() )
            }
        }
    }

    fun resetFailReason() {
        _failReason.value = " "
    }
}