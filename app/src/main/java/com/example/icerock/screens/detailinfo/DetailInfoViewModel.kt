package com.example.icerock.screens.detailinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icerock.constants.Constants
import com.example.icerock.repository.*
import com.example.icerock.repository.retrofit.ReadME
import com.example.icerock.repository.retrofit.Repo
import com.example.icerock.usecases.CheckInternetConnection
import com.example.icerock.usecases.GetListRepoByGitHub
import com.example.icerock.usecases.GetRepositoryReadme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailInfoViewModel @Inject constructor(
    private val internetConnection: CheckInternetConnection,
    private val getRepositoryReadme: GetRepositoryReadme,
    private val getListRepoByGitHub: GetListRepoByGitHub,
    private val keyValueStorage: KeyValueStorage) : ViewModel(){

    private var stateMutLive = MutableLiveData<State>()
    val state: LiveData<State> = stateMutLive

    private var readmeMutLive = MutableLiveData<ReadmeState>()
    val readme: LiveData<ReadmeState> = readmeMutLive

    private var nameMutLive = MutableLiveData<String>()
    val name: LiveData<String> = nameMutLive

    fun saveName(name: String){
        nameMutLive.value = name
    }

    init {
        getDetailInfo()
    }

    fun getDetailInfo(){
        stateMutLive.value = State.Loading
        viewModelScope.launch {
            try {
                val response: List<Repo> = getListRepoByGitHub.getRepoList(keyValueStorage.authToken)
                response.forEach {
                    if(it.name == nameMutLive.value){
                        stateMutLive.value = State.Loaded(it,ReadmeState.Loading)
                    }
                }
            } catch (e: HttpException) {
                if(e.code() == 401){
                    stateMutLive.value = State.Error(Constants.REQUIRES_AUTHENTICATION)
                }else if(e.code() == 403){
                    stateMutLive.value = State.Error(Constants.FORBIDDEN)
                }else if(e.code() == 404){
                    stateMutLive.value = State.Error(Constants.RESOURCE_NOT_FOUND)
                }else if(e.code() == 304){
                    stateMutLive.value = State.Error(Constants.NOT_MODIFIED)
                }else{
                    stateMutLive.value = State.Error(e.message.toString())
                }
            } catch (e: IOException){
                if(internetConnection.checkInternet()) {
                    stateMutLive.value = State.Error(Constants.CONNECTION_ERROR)
                }
            }
        }
    }

    fun getReadme(){
        readmeMutLive.value = ReadmeState.Loading
        viewModelScope.launch {
            try{
                val responseReadMe: ReadME = getRepositoryReadme.getRepositoryReadme(keyValueStorage.userName ?: "", nameMutLive.value!!,"master")
                readmeMutLive.value = ReadmeState.Loaded(responseReadMe.download_url!!)
            } catch (e: HttpException) {
                if(e.code() == 404){
                    readmeMutLive.value = ReadmeState.Empty
                }
            } catch (e: IOException){
                if(internetConnection.checkInternet()){
                    readmeMutLive.value = ReadmeState.Error(Constants.CONNECTION_ERROR)
                }
            }
        }
    }


    sealed interface DetailDataModel

    sealed interface State: DetailDataModel{
        object Loading : State
        data class Error(val error: String) : State
        data class Loaded(
            val githubRepo: Repo,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState: DetailDataModel {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

}