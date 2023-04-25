package com.devs.adminapplication.screens.orderHistory

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.screens.details.DetailScreenState
import com.devs.adminapplication.utils.MyPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderHistoryState(
    var orderHistory: List<OrderHistory> = emptyList(),

)

@HiltViewModel
class OrderHistoryViewmodel @Inject constructor(
    private val myPreference: MyPreference,
    private val api: AdminShopApi,
) : ViewModel() {
    private var _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _orderHistoryState = MutableStateFlow(OrderHistoryState())
    val orderHistoryState get() = _orderHistoryState.asStateFlow()
    fun getOrderHistory(fromDate: String, toDate: String) {
        Log.d("json2xls", "Path: " + fromDate + " " + toDate)
        Log.d("json2xls", "Path: " + fromDate.reversed() + " " + toDate.reversed())
        _loading.value=true
        viewModelScope.launch {
                val response=api.getOrderHistory(
                    token = myPreference.getStoredTag(),
                    fromDate = fromDate,
                    toDate = toDate
                )
            if (response.orderHistory!=null){
                _orderHistoryState.value=_orderHistoryState.value.copy(
                    orderHistory = response.orderHistory
                )
                _loading.value=false
            }

        }
    }
    fun resetOrderHistory(){
        _orderHistoryState.value=_orderHistoryState.value.copy(
            orderHistory = emptyList()
        )
    }
}