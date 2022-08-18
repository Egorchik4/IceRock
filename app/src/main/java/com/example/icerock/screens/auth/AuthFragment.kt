package com.example.icerock.screens.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.icerock.R
import com.example.icerock.activity.MainActivity
import com.example.icerock.databinding.AuthFragmentBinding
import com.example.icerock.usecases.ShowAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class AuthFragment : Fragment() {
    lateinit var binding: AuthFragmentBinding
    private val viewModelAuth: AuthViewModel by viewModels()
    @Inject lateinit var showAlertDialog: ShowAlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AuthFragmentBinding.inflate(inflater)
        MainActivity.exitFromApplication = false
        val token: String? = viewModelAuth.token.value
        if(token != null){
            findNavController().navigate(R.id.action_authFragment_to_repositoriesListFragment)
            viewModelAuth.clearToken()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelAuth.state.observe(viewLifecycleOwner){
            if (viewModelAuth.state.value != null){
                if(viewModelAuth.state.value is AuthViewModel.State.Idle) {
                    idleState()
                    findNavController().navigate(R.id.action_authFragment_to_repositoriesListFragment)
                    viewModelAuth.clearLive() // очистить LiveData для возврата на этот экран
                }else if(viewModelAuth.state.value is AuthViewModel.State.Loading) {
                    loadingState()
                }else if(viewModelAuth.state.value is AuthViewModel.State.InvalidInput) {
                    invalidInputState()
                    val message: String = (viewModelAuth.state.value as AuthViewModel.State.InvalidInput).reason  // достаём значение причины из data class
                    showAlertDialog.showDialog(message,requireContext())
                }
            }
        }

        binding.textViewInput.addTextChangedListener {
            binding.textViewFloat.setTextColor(resources.getColor(R.color.text_blue))
            binding.textViewInput.backgroundTintList = resources.getColorStateList(R.color.text_blue)
            binding.textViewFloat.visibility = View.VISIBLE
            binding.textViewHelper.visibility = View.INVISIBLE
        }

        binding.cardAuth.setOnClickListener {
            val textToken: String = binding.textViewInput.text.toString()
            viewModelAuth.onSignButtonPressed(textToken)
        }

        viewModelAuth.actions
            .onEach {
                if(it is AuthViewModel.Action.RouteToMain){

                }else if(it is AuthViewModel.Action.ShowError) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.textViewSign.visibility = View.VISIBLE
                    val message = it.message
                    showAlertDialog.showDialog(message,requireContext())
                }
            }.launchIn(lifecycleScope)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun idleState(){
        binding.textViewSign.hideKeyboard()
        binding.textViewHelper.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.INVISIBLE
        binding.textViewSign.visibility = View.VISIBLE
        binding.textViewFloat.visibility = View.INVISIBLE
    }
    private fun loadingState(){
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewSign.visibility = View.INVISIBLE
        binding.textViewHelper.visibility = View.INVISIBLE
    }
    private fun invalidInputState(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.textViewSign.visibility = View.VISIBLE

        binding.textViewHelper.visibility = View.VISIBLE
        binding.textViewFloat.setTextColor(resources.getColor(R.color.text_red))
        binding.textViewInput.backgroundTintList = resources.getColorStateList(R.color.text_red)
    }
}