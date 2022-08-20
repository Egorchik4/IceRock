package com.example.icerock.usecases

import android.content.SharedPreferences
import javax.inject.Inject

class PutInSharedPreference @Inject constructor(var prefput: SharedPreferences) {
    fun putInSharedPref(key: String, value: String){
        prefput
            .edit()
            .putString(key,value)
            .apply()
    }
}