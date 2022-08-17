package com.example.icerock.screens.repositorieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.icerock.R
import com.example.icerock.constants.Constants
import com.example.icerock.databinding.RecyclerItemBinding
import com.example.icerock.repository.Repo

class RepositoriesListRecyclerAdapter(private val listener: Listener): RecyclerView.Adapter<RepositoriesListRecyclerAdapter.RepoHolder>() {

    var repoList: MutableList<Repo> = mutableListOf()
    private val sizeList: Int = 10
    private fun addItem(newItem: Repo, index: Int){
        repoList.add(newItem)
        notifyItemChanged(index)
    }

    class RepoHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = RecyclerItemBinding.bind(item)
        fun bind(repo: Repo)
        {
            if(repo.language == Constants.KOTLIN){
                binding.textViewLanguage.setTextColor(binding.textViewLanguage.resources.getColor(R.color.kotlin_text))
            }else if(repo.language == Constants.JAVA_SCRIPT){
                binding.textViewLanguage.setTextColor(binding.textViewLanguage.resources.getColor(R.color.javascript_text))
            }else if(repo.language == Constants.JAVA){
                binding.textViewLanguage.setTextColor(binding.textViewLanguage.resources.getColor(R.color.java_text))
            }else{
                binding.textViewLanguage.setTextColor(binding.textViewLanguage.resources.getColor(R.color.white))
            }
            binding.textViewLanguage.text = repo.language
            binding.textViewName.text = repo.name
            binding.textViewLanguage.text = repo.language
            binding.textViewDescription.text = repo.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item,parent,false) // надуваем View(ячейку списка)
        return RepoHolder(view)
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bind(repoList[position])
        holder.binding.linearLayoutContainer.setOnClickListener {
            listener.myOnClick(repoList[position])
        }
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    fun addAll(list: List<Repo>){
        if(list.size >= sizeList){
            for (i in 0..(sizeList - 1)) {
                addItem(list[i], i)
            }
        }else{
            for (i in 0..(list.size - 1)) {
                addItem(list[i], i)
            }
        }
    }

    interface Listener{
        fun myOnClick(repo: Repo)
    }
}