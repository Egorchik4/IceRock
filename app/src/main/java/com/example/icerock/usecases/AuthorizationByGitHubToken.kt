package com.example.icerock.usecases

import com.example.icerock.repository.retrofit.UserInfo
import com.example.icerock.repository.retrofit.AppRepository
import javax.inject.Inject

class AuthorizationByGitHubToken @Inject constructor(var appRepository: AppRepository) {

    suspend fun autorization(token: String): UserInfo {
        return appRepository.signIn(token)
    }
}