package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.models.NewsData
import com.example.newsapp.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {
    fun execute(news: List<NewsData>) {
        newsRepository.saveNews(news = news)
    }
}