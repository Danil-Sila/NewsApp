package com.example.newsapp.domain.repository

import com.example.newsapp.domain.models.News

interface NewsRepository {
    fun getNews(): List<News>
    fun saveNews(news: List<News>)
}