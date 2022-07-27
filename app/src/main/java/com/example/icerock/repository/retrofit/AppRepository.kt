package com.example.icerock.repository.retrofit

import com.example.icerock.repository.ReadME
import com.example.icerock.repository.Repo
import com.example.icerock.repository.UserInfo
import retrofit2.http.*

interface AppRepository {

    @GET("user")
    suspend fun signIn(@Header("Authorization") token: String): UserInfo

    @GET("user/repos")
    suspend fun getRepositories(@Header("Authorization") token: String): List<Repo>

    @GET("repos/{owner}/{repo}/readme")
    suspend fun getRepositoryReadme(@Path("owner") ownerName: String, @Path("repo") repositoryName: String, @Query("ref") branchName: String): ReadME
}