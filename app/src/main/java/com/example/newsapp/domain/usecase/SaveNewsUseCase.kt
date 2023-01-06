package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.models.News
import com.example.newsapp.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {
    fun execute(news: List<News>) {
        newsRepository.saveNews(news = news)
    }
}