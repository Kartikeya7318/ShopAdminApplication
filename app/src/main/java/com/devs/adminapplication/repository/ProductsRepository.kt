package com.devs.adminapplication.repository

import android.util.Log
import com.devs.adminapplication.models.util.DataOrException
import com.devs.adminapplication.models.productResponse.Products
import com.devs.adminapplication.models.categories.CategoryList
import com.devs.adminapplication.models.subcategories.SubCategoryList
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.utils.Constants.JSON_DATA
import com.devs.adminapplication.utils.MyPreference
import com.google.gson.Gson
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

    fun getAllProductsLocal(): DataOrException<Products, Boolean, Exception> {
        var gson = Gson()
        var response = gson.fromJson(JSON_DATA, Products::class.java)
//        Assert.assertEquals(testModel.id, 1)
//        Assert.assertEquals(testModel.description, "Test")
        return DataOrException(data = response)
    }

    suspend fun getAllCategories(token: String): DataOrException<CategoryList, Boolean, Exception> {
        val response=try {
            Log.d("LoginFlow", "getAllCategories: "+token)
            api.getAllCategories(token=token)
        } catch (e:Exception){
            return DataOrException(e=e)
        }
        return DataOrException(data = response)
    }
    suspend fun getAllSubCategories(token: String): DataOrException<SubCategoryList, Boolean, Exception> {
        val response=try {
            Log.d("LoginFlow", "getAllCategories: "+token)
            api.getAllSubCategories(token=token)
        } catch (e:Exception){
            return DataOrException(e=e)
        }
        return DataOrException(data = response)
    }
}