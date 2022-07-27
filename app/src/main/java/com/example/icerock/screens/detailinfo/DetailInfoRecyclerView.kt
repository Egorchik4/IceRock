package com.example.icerock.screens.detailinfo

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import br.tiagohm.markdownview.MarkdownView
import br.tiagohm.markdownview.css.InternalStyleSheet
import br.tiagohm.markdownview.css.styles.Github
import com.example.icerock.R
import com.example.icerock.constants.Constants
import com.example.icerock.databinding.RecyclerItemInfoBinding
import com.example.icerock.databinding.RecyclerItemReadmeBinding


class DetailInfoRecyclerView : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var arraylist: Array<DetailInfoViewModel.DetailDataModel?> = arrayOf(null,null)

    private fun refreshState(newState: DetailInfoViewModel.State, index: Int){
        arraylist[index] = newState
        notifyItemChanged(index)
    }

    private fun refreshReadmeState(state: DetailInfoViewModel.ReadmeState, index: Int){
        arraylist[index] = state
        notifyItemChanged(index)
    }

    class StateDetailHolder(item: View): RecyclerView.ViewHolder(item){
        val binding = RecyclerItemInfoBinding.bind(item)
        fun bind(state: DetailInfoViewModel.DetailDataModel){
            if(state is DetailInfoViewModel.State.Loaded){
                binding.textViewForks.text = state.githubRepo.forks_count.toString()
                binding.textViewLicense.text = state.githubRepo.license?.name ?: Constants.NO_LICENCE
                binding.textViewURL.text = state.githubRepo.html_url
                binding.textViewStars.text = state.githubRepo.stargazers_count.toString()
                binding.textViewWatchers.text = state.githubRepo.watchers_count.toString()

                binding.LinearURL.setOnClickListener {
                    val url = state.githubRepo.html_url
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(binding.textViewURL.context,i,null)
                }


            }

        }
    }

    class ReadmeDetailHolder(item: View): RecyclerView.ViewHolder(item){
        val bindingg = RecyclerItemReadmeBinding.bind(item)
        fun bind(state: DetailInfoViewModel.DetailDataModel){
            if(state is DetailInfoViewModel.ReadmeState.Loading){
                bindingg.progressBarReadMe.visibility = View.VISIBLE
                bindingg.textViewReadMeEmpty.visibility = View.GONE
                bindingg.ConnectionErrorLayout.visibility = View.GONE

            }else if(state is DetailInfoViewModel.ReadmeState.Empty) {
                bindingg.ViewReadMe.visibility = View.GONE
                bindingg.textViewReadMeEmpty.visibility = View.VISIBLE
                bindingg.progressBarReadMe.visibility = View.GONE
                bindingg.ConnectionErrorLayout.visibility = View.GONE

            }else if(state is DetailInfoViewModel.ReadmeState.Loaded){
                bindingg.textViewReadMeEmpty.visibility = View.GONE
                bindingg.progressBarReadMe.visibility = View.GONE
                bindingg.ViewReadMe.visibility = View.VISIBLE
                bindingg.ConnectionErrorLayout.visibility = View.GONE
                renderMarkdown(bindingg.ViewReadMe,state)
            }else if(state is DetailInfoViewModel.ReadmeState.Error){
                if(state.error == Constants.CONNECTION_ERROR) {
                    bindingg.ViewReadMe.visibility = View.GONE
                    bindingg.textViewReadMeEmpty.visibility = View.GONE
                    bindingg.progressBarReadMe.visibility = View.GONE
                    bindingg.ConnectionErrorLayout.visibility = View.VISIBLE
                }
            }
        }

        private fun renderMarkdown(view: View, state: DetailInfoViewModel.ReadmeState.Loaded){
            val markdownView: MarkdownView = view as MarkdownView
            val css: InternalStyleSheet = Github()
            css.addRule("*", "color: white")
            css.addRule("body","background: black")
            css.addRule("*","text-align: left")
            css.addRule("body","margin: 0")
            css.addRule("body","padding: 0")
            markdownView.addStyleSheet(css)
            markdownView.loadMarkdownFromUrl(state.markdown)
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = 0
        if (position == 0) {
            viewType = ViewType.DetailHolder.type
        }else if(position == 1){
            viewType = ViewType.ReadMEHolder.type
        }
        return viewType
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        if(viewType == ViewType.DetailHolder.type)
        {
            val layout = R.layout.recycler_item_info
            val view = LayoutInflater
                .from(parent.context)
                .inflate(layout, parent, false)
            viewHolder = StateDetailHolder(view)
        }else if(viewType == ViewType.ReadMEHolder.type) {
            val layout = R.layout.recycler_item_readme
            val view = LayoutInflater
                .from(parent.context)
                .inflate(layout, parent, false)
            viewHolder = ReadmeDetailHolder(view)
        }
        return viewHolder

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == ViewType.DetailHolder.type){
                if(arraylist[position] != null){
                    (holder as StateDetailHolder).bind(arraylist[position]!!)
                }
        }else if(getItemViewType(position) == ViewType.ReadMEHolder.type){
            if(arraylist[position] != null){
                (holder as ReadmeDetailHolder).bind(arraylist[position]!!)
            }
        }
    }

    override fun getItemCount(): Int {
        return if(arraylist[0] != null && arraylist[1] != null){
            2
        }else if(arraylist[0] != null){
            1
        }else{
            0
        }
    }

    fun addState(readmeState: DetailInfoViewModel.ReadmeState){
        refreshReadmeState(readmeState,1)
    }

    fun addInfo(state: DetailInfoViewModel.State){
        refreshState(state,0)
    }

    enum class ViewType(var type: Int){
        DetailHolder(0),
        ReadMEHolder(1)
    }


}