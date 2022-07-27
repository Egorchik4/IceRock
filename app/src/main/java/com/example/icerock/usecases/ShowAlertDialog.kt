package com.example.icerock.usecases

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import javax.inject.Inject

class ShowAlertDialog @Inject constructor() {

    fun showDialog(message: String, context: Context){
        val dialog = AlertDialog.Builder(context)
            .setCancelable(true)
            .setTitle("Error")
            .setMessage(message)
            .setNegativeButton("Ok"){_,_->
            }
            .create()

        dialog.show()
    }
}