package com.example.newsapp.domain.repository

import com.example.newsapp.domain.models.News

interface NewsRepository {
    fun getNews(modeNewsHide: Boolean): List<News>
    fun saveNews(news: List<News>)
    suspend fun deleteNews(news: News, onSuccess: () -> Unit)
    suspend fun setVisibleNews(news: News, onSuccess: () -> Unit)
}