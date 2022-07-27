package com.example.icerock.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.icerock.constants.Constants
import javax.inject.Inject

class SharedPrefStorage @Inject constructor(var context: Context) {
    var prefStorage: SharedPreferences = context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
}