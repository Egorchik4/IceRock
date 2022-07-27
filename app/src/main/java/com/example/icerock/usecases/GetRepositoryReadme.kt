package com.example.icerock.usecases

import com.example.icerock.repository.ReadME
import com.example.icerock.repository.retrofit.AppRepository
import javax.inject.Inject

class GetRepositoryReadme @Inject constructor(var app: AppRepository) {

    suspend fun getRepositoryReadme(ownerName: String,repositoryName: String,branchName: String): ReadME {
        return app.getRepositoryReadme(ownerName, repositoryName, branchName)
    }
}