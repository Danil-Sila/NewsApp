package com.example.newsapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.NewsItemBinding
import com.example.newsapp.data.database.models.News
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

interface NewsListListener {
    fun onClickNews(news: News)
    fun onDeleteNews(news: News)
    fun onHideNews(news: News)
}

class NewsAdapter(private val newsListListener: NewsListListener): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>(),
    Filterable, View.OnClickListener {

    private var listNews = emptyList<News>()
    private var listNewsFiltered = emptyList<News>()

    class NewsViewHolder(val binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val news = v.tag as News
        when(v.id) {
            R.id.more -> {
                showPopupMenu(v)
            }
            else -> {
                newsListListener.onClickNews(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        binding.more.setOnClickListener(this)
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
                    holder.itemView.tag = listNewsFiltered[position]
                    binding.more.tag = listNewsFiltered[position]
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                listNewsFiltered = if (charSearch.isEmpty()) {
                    listNews as ArrayList<News>
                } else {
                    val resultList = ArrayList<News>()
                    for (row in listNews) {
                        if (row.title?.lowercase()?.contains(constraint.toString().lowercase())!!) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listNewsFiltered
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    listNewsFiltered = results.values as ArrayList<News>
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun showPopupMenu(v: View) {
        val popupMenu = PopupMenu(v.context, v)
        val news = v.tag as News
        popupMenu.menu.add(0, ID_DELETE_NEWS, Menu.NONE, "Удалить новость")
        if (news.hide_news) {
            popupMenu.menu.add(0, ID_HIDE_NEWS, Menu.NONE, "Отобразить новость")
        } else {
            popupMenu.menu.add(0, ID_HIDE_NEWS, Menu.NONE, "Скрыть новость")
        }
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                ID_DELETE_NEWS -> {
                    newsListListener.onDeleteNews(news)
                }
                ID_HIDE_NEWS -> {
                    news.hide_news = !news.hide_news
                    newsListListener.onHideNews(news)
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_DELETE_NEWS = 1
        private const val ID_HIDE_NEWS = 2
    }

}