package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.models.News
import com.example.newsapp.domain.repository.NewsRepository

class SetVisibleNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(news: News, onSuccess: () -> Unit) {
        newsRepository.setVisibleNews(news = news, onSuccess = onSuccess)
    }
}