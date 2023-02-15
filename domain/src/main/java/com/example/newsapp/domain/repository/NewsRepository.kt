package com.example.newsapp.domain.repository

import com.example.newsapp.domain.models.NewsData

interface NewsRepository {
    fun getNews(modeNewsHide: Boolean): List<NewsData>
    fun saveNews(news: List<NewsData>)
    suspend fun deleteNews(news: NewsData, onSuccess: () -> Unit)
    suspend fun setVisibleNews(news: NewsData, onSuccess: () -> Unit)
    fun getNewsFromService(newsFromServiceState: LoadNewsFromServiceListener)
}

interface LoadNewsFromServiceListener {
    fun idSuccess(news: List<NewsData>)
    fun isError(message: String)
}