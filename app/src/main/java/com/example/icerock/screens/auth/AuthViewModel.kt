package com.example.icerock.screens.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icerock.constants.Constants
import com.example.icerock.repository.KeyValueStorage
import com.example.icerock.repository.retrofit.UserInfo
import com.example.icerock.usecases.AuthorizationByGitHubToken
import com.example.icerock.usecases.CheckInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val internetConnection: CheckInternetConnection,
    private val authorizationByGitHubToken: AuthorizationByGitHubToken,
    private val keyValueStorage: KeyValueStorage) : ViewModel() {

    private var tokenMutLive = MutableLiveData<String?>()
    val token: LiveData<String?> = tokenMutLive

    private var stateMutLive = MutableLiveData<State?>()
    val state: LiveData<State?> = stateMutLive

    private var actionsMut = MutableStateFlow<Action>(Action.RouteToMain)
    val actions: StateFlow<Action> = actionsMut.asStateFlow()

    init{
        tokenMutLive.value = keyValueStorage.authToken
    }

    fun onSignButtonPressed(idToken: String){
        if(idToken.length == 40 && idToken.isNotEmpty() && checkCorrectInput(idToken)){
            val token: String = Constants.INITIAL_PART_TOKEN + idToken
            stateMutLive.value = State.Loading
            actionsMut.value = Action.RouteToMain
            viewModelScope.launch{
                try {
                    val responseToRetrofit: UserInfo = authorizationByGitHubToken.autorization(token)
                    tokenMutLive.value = token
                    stateMutLive.value = State.Idle

                    keyValueStorage.putValueInSharedPref(Constants.KEY_TOKEN,token)
                    keyValueStorage.putValueInSharedPref(Constants.NAME,responseToRetrofit.login)

                    actionsMut.value = Action.RouteToMain

                } catch (e: HttpException) {
                    if(e.code() == 401){
                        stateMutLive.value = State.InvalidInput(Constants.REQUIRES_AUTHENTICATION)
                    }else if(e.code() == 403){
                        stateMutLive.value = State.InvalidInput(Constants.FORBIDDEN)
                    }else if(e.code() == 404){
                        stateMutLive.value = State.InvalidInput(Constants.RESOURCE_NOT_FOUND)
                    }else if(e.code() == 304){
                        stateMutLive.value = State.InvalidInput(Constants.NOT_MODIFIED)
                    }else{
                        stateMutLive.value = State.InvalidInput(e.message.toString())
                    }
                } catch (e: IOException){
                    if(internetConnection.checkInternet()){
                        actionsMut.value = Action.ShowError(Constants.CONNECTION_ERROR)
                    }

                }
            }
        }else{
            stateMutLive.value = State.InvalidInput(Constants.INVALID_INPUT)
        }

    }

    private fun checkCorrectInput(text: String): Boolean{
        for(char in text){
            if (Character.UnicodeBlock.of(char) == Character.UnicodeBlock.CYRILLIC && char !in 'A'..'Z' && char !in 'a'..'z' && char != '_') {
                return false
            }
        }
        return true
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String) : Action
        object RouteToMain : Action
    }

    fun clearLive(){
        stateMutLive.value = null
        clearToken()

    }

    fun clearToken(){
        tokenMutLive.value = null
    }
}