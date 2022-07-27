package com.example.icerock.usecases

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class ShowToastMessage @Inject constructor() {

    fun showToast(message: String?,context: Context){
        if (message != null){
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }
    }
}