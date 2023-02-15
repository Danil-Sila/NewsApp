package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.models.NewsData
import com.example.newsapp.domain.repository.NewsRepository

class GetNewsUseCase(private val newsRepository: NewsRepository) {
    fun execute(modeNewsHide: Boolean): List<NewsData> {
        return newsRepository.getNews(modeNewsHide = modeNewsHide)
    }
}