package com.example.icerock.di

import android.content.Context
import android.content.SharedPreferences
import com.example.icerock.repository.retrofit.RetrofitFactoryAppRepo
import com.example.icerock.repository.SharedPrefStorage
import com.example.icerock.repository.retrofit.AppRepository
import com.example.icerock.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun provideCheckInternetConnection(@ApplicationContext context: Context): CheckInternetConnection{
        return CheckInternetConnection(context)
    }

    @Provides
    @Singleton
    fun provideSharedPrefStorage(@ApplicationContext context: Context): SharedPreferences{
        return SharedPrefStorage(context).prefStorage
    }


    @Provides
    @Singleton
    fun provideRetrofitGsonFactory(): AppRepository {
        return RetrofitFactoryAppRepo().appRepository
    }
}