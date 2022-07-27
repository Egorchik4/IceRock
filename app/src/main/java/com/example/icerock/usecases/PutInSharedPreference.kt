package com.example.icerock.usecases

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PutInSharedPreference @Inject constructor(var prefput: SharedPreferences) {
    fun putinshared(key: String,value: String){
        // запись в sharedpreference
        prefput
            .edit()
            .putString(key,value)
            .apply()
    }
}