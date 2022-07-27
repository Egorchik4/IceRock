package com.example.icerock.screens.repositorieslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icerock.R
import com.example.icerock.activity.MainActivity
import com.example.icerock.constants.*
import com.example.icerock.databinding.RepositoriesListFragmentBinding
import com.example.icerock.repository.Repo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RepositoriesListFragment : Fragment(), RepositoriesListRecyclerAdapter.Listener{
    lateinit var binding: RepositoriesListFragmentBinding
    private val viewModelRepo: RepositoriesListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = RepositoriesListFragmentBinding.inflate(inflater)
        MainActivity.exitFromApplication = true
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewExit.setOnClickListener {
            findNavController().popBackStack()
        }

        val adapter = RepositoriesListRecyclerAdapter(this)
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.rcView.adapter = adapter


        viewModelRepo.stateRepo.observe(viewLifecycleOwner){
            if(viewModelRepo.stateRepo.value is RepositoriesListViewModel.StateRepo.Loaded){
                idleState()
                val listRepo: List<Repo> = (viewModelRepo.stateRepo.value as RepositoriesListViewModel.StateRepo.Loaded).repos
                adapter.addAll(listRepo)
            }else if(viewModelRepo.stateRepo.value is RepositoriesListViewModel.StateRepo.Loading) {
                loadingState()
            }else if(viewModelRepo.stateRepo.value is RepositoriesListViewModel.StateRepo.Error) {
                val errorType: String = (viewModelRepo.stateRepo.value as RepositoriesListViewModel.StateRepo.Error).error
                invalidInputState(errorType)
            }else if(viewModelRepo.stateRepo.value is RepositoriesListViewModel.StateRepo.Empty){
                emptyState()
            }
        }

        binding.CardViewRefresh.setOnClickListener {
            viewModelRepo.getListRepository()
        }
    }

    override fun myOnClick(repo: Repo) {
        val bundle = Bundle()
        bundle.putString(Constants.NAME_OF_REPO,repo.name)
        parentFragmentManager.setFragmentResult(Constants.NAME,bundle)

        findNavController().navigate(R.id.action_repositoriesListFragment_to_detailInfoFragment)
    }

    private fun idleState(){
        binding.progressBarList.visibility = View.GONE
        binding.CardViewRefresh.visibility = View.GONE
    }
    private fun loadingState(){
        binding.progressBarList.visibility = View.VISIBLE
        binding.CardViewRefresh.visibility = View.GONE
    }

    private fun invalidInputState(errorType: String){
        binding.progressBarList.visibility = View.GONE
        binding.CardViewRefresh.visibility = View.VISIBLE
        binding.textViewRefresh.text = Constants.RETRY
        if(errorType == Constants.CONNECTION_ERROR){
            binding.ConnectionErrorLayout.visibility = View.VISIBLE
            binding.SomethingErrorLayout.visibility = View.GONE
        }else{
            binding.SomethingErrorLayout.visibility = View.VISIBLE
            binding.ConnectionErrorLayout.visibility = View.GONE
        }
        binding.EmptyLayout.visibility = View.GONE
    }

    private fun emptyState(){
        binding.progressBarList.visibility = View.GONE
        binding.EmptyLayout.visibility = View.VISIBLE

        binding.CardViewRefresh.visibility = View.VISIBLE
        binding.textViewRefresh.text = Constants.REFRESH

        binding.SomethingErrorLayout.visibility = View.GONE
        binding.ConnectionErrorLayout.visibility = View.GONE
    }

}