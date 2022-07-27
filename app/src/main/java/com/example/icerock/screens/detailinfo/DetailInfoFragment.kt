package com.example.icerock.screens.detailinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icerock.R
import com.example.icerock.activity.MainActivity
import com.example.icerock.constants.*
import com.example.icerock.databinding.DetailInfoFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailInfoFragment : Fragment() {
    lateinit var binding: DetailInfoFragmentBinding
    private val viewModelDetailInfo: DetailInfoViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DetailInfoFragmentBinding.inflate(inflater)
        MainActivity.exitFromApplication = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(Constants.NAME,viewLifecycleOwner){_, data ->
            val name: String? = data.getString(Constants.NAME_OF_REPO)
            viewModelDetailInfo.saveName(name)
            binding.textViewNameRepo.text = viewModelDetailInfo.nameDeatail
        }

        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imageViewfromDetailtoAuth.setOnClickListener {
            findNavController().popBackStack(R.id.authFragment,false)
        }

        val adapter = DetailInfoRecyclerView()  // создание объекта класса
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())  // LinearLayoutManager: упорядочивает элементы в виде списка с одной колонкой
        binding.recycler.adapter = adapter



        viewModelDetailInfo.state.observe(viewLifecycleOwner){
           if(viewModelDetailInfo.state.value is DetailInfoViewModel.State.Loading){
               loadingState()
           }else if(viewModelDetailInfo.state.value is DetailInfoViewModel.State.Error) {
               val errorType: String = (viewModelDetailInfo.state.value as DetailInfoViewModel.State.Error).error
               invalidInputState(errorType)
           }else if(viewModelDetailInfo.state.value is DetailInfoViewModel.State.Loaded) {
               idleState()
               adapter.addInfo(viewModelDetailInfo.state.value as DetailInfoViewModel.State.Loaded)
           }
        }

        viewModelDetailInfo.readme.observe(viewLifecycleOwner){
            if(viewModelDetailInfo.readme.value is DetailInfoViewModel.ReadmeState.Error){
                binding.CardViewRefreshDetail.visibility = View.VISIBLE
            }else{
                binding.CardViewRefreshDetail.visibility = View.GONE
            }
            adapter.addState(viewModelDetailInfo.readme.value!!)
        }

        binding.CardViewRefreshDetail.setOnClickListener {
            if(viewModelDetailInfo.readme.value is DetailInfoViewModel.ReadmeState.Error){
                viewModelDetailInfo.getReadme()
            }else{
                viewModelDetailInfo.getDetailInfo()
            }
        }

    }

    private fun idleState(){
        binding.recycler.visibility = View.VISIBLE
        binding.progressBarDetail.visibility = View.GONE
        binding.ConnectionErrorLayout.visibility = View.GONE
        binding.CardViewRefreshDetail.visibility = View.GONE

        viewModelDetailInfo.getReadme()
    }
    private fun loadingState(){
        binding.progressBarDetail.visibility = View.VISIBLE
        binding.recycler.visibility = View.GONE
        binding.ConnectionErrorLayout.visibility = View.GONE
        binding.CardViewRefreshDetail.visibility = View.GONE
    }
    private fun invalidInputState(errorType: String){
        if(errorType == Constants.CONNECTION_ERROR){
            binding.ConnectionErrorLayout.visibility = View.VISIBLE
            binding.CardViewRefreshDetail.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
            binding.progressBarDetail.visibility = View.GONE
        }
    }


}