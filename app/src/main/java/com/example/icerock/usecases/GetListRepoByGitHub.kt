package com.example.icerock.usecases

import com.example.icerock.repository.retrofit.Repo
import com.example.icerock.repository.retrofit.AppRepository
import javax.inject.Inject

class GetListRepoByGitHub @Inject constructor(var app: AppRepository) {
    suspend fun getRepoList(token: String?): List<Repo>{
        return app.getRepositories(token ?:"")
    }
}