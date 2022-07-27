package com.example.icerock.usecases

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class GetInSharedPreference @Inject constructor(var prefget: SharedPreferences) {
    fun getinshared(key: String): String?{
        return prefget.getString(key,null)
    }
}