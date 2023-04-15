package com.devs.adminapplication.repository

import android.util.Log
import com.devs.adminapplication.models.categories.BrandList
import com.devs.adminapplication.models.categories.CategoryList
import com.devs.adminapplication.models.productResponse.Products
import com.devs.adminapplication.models.subcategories.SubCategoryList
import com.devs.adminapplication.models.util.DataOrException
import com.devs.adminapplication.network.AdminShopApi
import com.devs.adminapplication.utils.MyPreference
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val api: AdminShopApi,private val myPreferences: MyPreference) {
    suspend fun getProducts(token: String,subCategoryId:String): DataOrException<Products, Boolean, Exception>{
        val response=try {
            Log.d("LoginFlow", "getAllProducts: "+token)
            api.getProducts(token=token,subCategoryId=subCategoryId.toLong(), brandId = 0)
        } catch (e:Exception){
            return DataOrException(e=e)
        }
        return DataOrException(data = response)
    }
    suspend fun getProduct(token: String,productId:String): DataOrException<Products, Boolean, Exception>{
        val response=try {
            Log.d("LoginFlow", "getAllProducts: "+token)
            api.getProduct(token=token,productId=productId.toInt())
        } catch (e:Exception){
            return DataOrException(e=e)
        }
        return DataOrException(data = response)
    }
    suspend fun getAllProducts(token:String): DataOrException<Products, Boolean, Exception> {
        val response=try {
            Log.d("LoginFlow", "getAllProducts: "+token)
            api.getAllProducts(token=token)
        } catch (e:Exception){
            return DataOrException(e=e)
        }
        return DataOrException(data = response)
    }



    suspend fun getAllBrands(token: String): DataOrException<BrandList, Boolean, Exception> {
        val response=try {
            Log.d("LoginFlow", "getAllCategories: "+token)
            api.getAllBrands(token=token)
        } catch (e:Exception){
            return DataOrException(e=e)
        }
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