package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.models.NewsData
import com.example.newsapp.domain.repository.NewsRepository

class SetVisibleNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(news: NewsData, onSuccess: () -> Unit) {
        newsRepository.setVisibleNews(news = news, onSuccess = onSuccess)
    }
}