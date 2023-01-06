package com.example.newsapp.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.NewsItemBinding
import com.example.newsapp.domain.models.News
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(private val newsListListener: NewsListListener): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(), Filterable {

    private var listNews = emptyList<News>()
    private var listNewsFiltered = emptyList<News>()

    class NewsViewHolder(val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        with(holder) {
            with(listNewsFiltered[position]) {
                val date =  SimpleDateFormat("dd-MM-yyyy").format(Date(news_date_uts!!.toLong()*1000))
                binding.dateNews.text = date
                binding.titleNews.text = title
                Glide.with(itemView.context).load(img).into(binding.imageNews)
                itemView.setOnClickListener {
                    newsListListener.onClickNews(listNews[position])
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listNewsFiltered.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNewsList(list: List<News>) {
        listNews = list
        listNewsFiltered = listNews
        notifyDataSetChanged()
    }

    interface NewsListListener {
        fun onClickNews(news: News)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    listNewsFiltered = listNews as ArrayList<News>
                } else {
                    val resultList = ArrayList<News>()
                    for (row in listNews) {
                        if (row.title?.lowercase()?.contains(constraint.toString().lowercase())!!) {
                            resultList.add(row)
                        }
                    }
                    listNewsFiltered = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listNewsFiltered
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    listNewsFiltered = results?.values as ArrayList<News>
                }
                notifyDataSetChanged()
            }
        }
    }

}