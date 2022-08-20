package com.example.icerock.repository.retrofit

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: Int,
    val login: String
    )
@Serializable
data class Repo(
    val id: Int,
    val name: String,
    val language: String?,
    val html_url: String,
    val description: String?,
    val forks_count: Int,
    val stargazers_count: Int,
    val watchers_count: Int,
    val license: License?
    )
@Serializable
data class ReadME(
    val download_url: String?
    )

@Serializable
data class License(
    val name: String
)

