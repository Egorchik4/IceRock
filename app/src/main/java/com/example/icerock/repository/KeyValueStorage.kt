package com.example.icerock.repository

import android.content.Context
import android.util.Log
import com.example.icerock.constants.Constants
import com.example.icerock.usecases.GetInSharedPreference
import com.example.icerock.usecases.PutInSharedPreference
import javax.inject.Inject


class KeyValueStorage @Inject constructor(
    var prefPutInStorage: PutInSharedPreference,
    var prefGetInStorage: GetInSharedPreference
    )
{
    var authToken: String? = null
    var userName: String? = null

    init{
        getAuthToken()
    }

    fun putValueInSharedPref(key: String, value: String){
        prefPutInStorage.putinshared(key, value)
    }

    private fun getAuthToken(){
        authToken = prefGetInStorage.getinshared(Constants.KEY_TOKEN)
        userName = prefGetInStorage.getinshared(Constants.NAME)
    }
}