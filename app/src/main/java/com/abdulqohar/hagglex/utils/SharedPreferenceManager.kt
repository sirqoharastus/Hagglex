package com.abdulqohar.hagglex.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(private val context: Context) {
       private val sharedPreferences = context.getSharedPreferences("SharedPref", Context.MODE_PRIVATE)

    fun saveToken(token:String){
        sharedPreferences.edit().putString(TOKENKEY, token).apply()
    }

    fun getToken(tokenKey:String): String? {
        return sharedPreferences.getString(tokenKey, null)
    }

}