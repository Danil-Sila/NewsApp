package com.example.newsapp.data.repository

import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.domain.models.News
import com.example.newsapp.domain.repository.NewsRepository

class NewsRepositoryImpl(private val newsDao: NewsDao): NewsRepository {
    override fun getNews(modeNewsHide: Boolean): List<News> {
       return newsDao.getAllNews(getNewsMode(modeNewsHide))
    }

    private fun getNewsMode(modeNewsHide: Boolean): Int {
        return if (modeNewsHide) {
            1
        } else {
            0
        }
    }

    override fun saveNews(news: List<News>) {
        newsDao.saveNews(news = news)
    }

    override suspend fun deleteNews(news: News, onSuccess: () -> Unit) {
        newsDao.deleteNews(news = news)
    }

    override suspend fun setVisibleNews(news: News, onSuccess: () -> Unit) {
        newsDao.setVisible(news = news)
    }
}