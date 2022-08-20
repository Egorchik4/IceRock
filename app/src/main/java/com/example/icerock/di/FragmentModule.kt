package com.example.icerock.di

import android.content.Context
import android.content.SharedPreferences
import com.example.icerock.repository.*
import com.example.icerock.repository.retrofit.*
import com.example.icerock.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Provides
    fun provideGetInSharedPreference(sharedPreferences: SharedPreferences): GetInSharedPreference {
        return GetInSharedPreference(sharedPreferences)
    }

    @Provides
    fun providePutInSharedPreference(sharedPreferences: SharedPreferences): PutInSharedPreference {
        return PutInSharedPreference(sharedPreferences)
    }

    @Provides
    fun provideKeyValueStorage(putInSharedPreference: PutInSharedPreference, getInSharedPreference: GetInSharedPreference): KeyValueStorage{
        return KeyValueStorage(putInSharedPreference, getInSharedPreference)
    }

    @Provides
    fun provideShowAlertDialog(): ShowAlertDialog {
        return ShowAlertDialog()
    }

    @Provides
    fun provideShowToastMessage(): ShowToastMessage {
        return ShowToastMessage()
    }

    @Provides
    fun provideAuthorizationByGitHubToken(appRepo: AppRepository): AuthorizationByGitHubToken{
        return AuthorizationByGitHubToken(appRepo)
    }

    @Provides
    fun provideGetListRepoByGitHub(appRepo: AppRepository): GetListRepoByGitHub{
        return GetListRepoByGitHub(appRepo)
    }

    @Provides
    fun provideGetRepositoryReadme(appRepo: AppRepository): GetRepositoryReadme{
        return GetRepositoryReadme(appRepo)
    }

}