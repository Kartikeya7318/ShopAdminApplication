package com.devs.adminapplication.repository

import android.util.Log
import com.devs.adminapplication.models.DataOrException
import com.devs.adminapplication.models.Products
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.utils.MyPreference
import retrofit2.Response
import retrofit2.http.Header
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val api: AdminShopApi,private val myPreferences: MyPreference) {
    suspend fun getAllProducts(token:String): DataOrException<Products, Boolean, Exception> {
        val response=try {
            Log.d("LoginFlow", "getAllProducts: "+token)
            api.getAllProducts(token=token)
        } catch (e:Exception){
            return DataOrException(e=e)
        }
        return DataOrException(data = response)
    }
}