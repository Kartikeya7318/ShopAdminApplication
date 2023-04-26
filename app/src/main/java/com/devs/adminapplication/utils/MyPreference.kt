package com.devs.adminapplication.utils

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(@ApplicationContext context: Context) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getStoredTag(): String {
        return prefs.getString(Constants.USER_TOKEN, "")!!
    }

    fun setStoredTag(query: String) {
        prefs.edit().putString(Constants.USER_TOKEN, query).apply()
    }

    fun setUserIdandPass(userid: String, pass: String) {
        prefs.edit().putString(Constants.USER_ID, userid).apply()
        prefs.edit().putString(Constants.PASSWORD, pass).apply()
    }

    fun getUserId(): String {
        return if (prefs.getString(
                Constants.USER_ID,
                ""
            ) == null
        ) "" else prefs.getString(Constants.USER_ID, "")!!
    }

    fun getPass(): String {
        return if (prefs.getString(
                Constants.PASSWORD, ""
            ) == null
        ) "" else prefs.getString(Constants.PASSWORD, "")!!
    }
}