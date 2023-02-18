package com.devs.adminapplication

import android.content.Context
import androidx.preference.PreferenceManager
import com.devs.adminapplication.retrofit.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context : Context){
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getStoredTag(): String {
        return prefs.getString(Constants.USER_TOKEN, "")!!
    }
    fun setStoredTag(query: String) {
        prefs.edit().putString(Constants.USER_TOKEN, query).apply()
    }
}