package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.models.News
import com.example.newsapp.domain.repository.NewsRepository

class GetNewsUseCase(private val newsRepository: NewsRepository) {
    fun execute(): List<News> {
        return newsRepository.getNews()
    }
}