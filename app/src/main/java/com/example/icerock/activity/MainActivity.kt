package com.example.icerock.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.icerock.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreen)   // экран загрузки
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if(exitFromApplication){
            finish()
        }else{
            super.onBackPressed()
        }
    }

    companion object{
        var exitFromApplication: Boolean = false
    }
}