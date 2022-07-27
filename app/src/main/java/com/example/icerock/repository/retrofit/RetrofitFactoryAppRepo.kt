package com.example.icerock.repository.retrofit

import com.example.icerock.constants.Constants
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Inject

class RetrofitFactoryAppRepo @Inject constructor(){

    private val url: String = Constants.GITHUB
    private val contentType = MediaType.get("application/json; charset=utf-8")
    private val json = Json{ignoreUnknownKeys = true}
    private val factory: Converter.Factory = json.asConverterFactory(contentType)

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(factory)
        .baseUrl(url)
        .build()

    var appRepository: AppRepository = retrofit.create(AppRepository::class.java)

}