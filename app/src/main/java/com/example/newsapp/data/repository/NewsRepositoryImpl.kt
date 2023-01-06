package com.example.newsapp.data.repository

import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.domain.models.News
import com.example.newsapp.domain.repository.NewsRepository

class NewsRepositoryImpl(private val newsDao: NewsDao): NewsRepository {
    override fun getNews(): List<News> {
       return newsDao.getAllNews()
    }

    override fun saveNews(news: List<News>) {
        newsDao.saveNews(news = news)
    }
}