package com.example.icerock.screens.repositorieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icerock.constants.Constants
import com.example.icerock.repository.KeyValueStorage
import com.example.icerock.repository.Repo
import com.example.icerock.usecases.CheckInternetConnection
import com.example.icerock.usecases.GetListRepoByGitHub
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val internetConnection: CheckInternetConnection,
    private val getListRepoByGitHub: GetListRepoByGitHub,
    private val keyValueStorage: KeyValueStorage) : ViewModel() {

    private var stateRepoMutLive = MutableLiveData<StateRepo?>()
    val stateRepo: LiveData<StateRepo?> = stateRepoMutLive

    init{
        getListRepository()
    }

    fun getListRepository(){
        stateRepoMutLive.value = StateRepo.Loading
        viewModelScope.launch{
            try {
                val response: List<Repo> = getListRepoByGitHub.getRepoList(keyValueStorage.authToken)
                stateRepoMutLive.value = StateRepo.Loaded(response)
            } catch (e: HttpException) {
                if(e.code() == 401){
                    stateRepoMutLive.value = StateRepo.Error(Constants.REQUIRES_AUTHENTICATION)
                }else if(e.code() == 403){
                    stateRepoMutLive.value = StateRepo.Error(Constants.FORBIDDEN)
                }else if(e.code() == 404){
                    stateRepoMutLive.value = StateRepo.Empty
                }else if(e.code() == 304){
                    stateRepoMutLive.value = StateRepo.Error(Constants.RESOURCE_NOT_FOUND)
                }else{
                    stateRepoMutLive.value = StateRepo.Error(e.message.toString())
                }
            } catch (e: IOException){
                if(internetConnection.checkInternet()){
                    stateRepoMutLive.value = StateRepo.Error(Constants.CONNECTION_ERROR)
                }
            }
        }
    }

    sealed interface StateRepo {
        object Loading : StateRepo
        data class Loaded(val repos: List<Repo>) : StateRepo
        data class Error(val error: String) : StateRepo
        object Empty : StateRepo
    }
}