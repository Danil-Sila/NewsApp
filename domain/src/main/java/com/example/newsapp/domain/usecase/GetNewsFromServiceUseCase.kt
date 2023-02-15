package com.example.newsapp.domain.usecase

import com.example.newsapp.domain.repository.LoadNewsFromServiceListener
import com.example.newsapp.domain.repository.NewsRepository

class GetNewsFromServiceUseCase(private val newsRepository: NewsRepository) {
    fun execute(newsFromServiceState: LoadNewsFromServiceListener) {
        newsRepository.getNewsFromService(newsFromServiceState = newsFromServiceState)
    }
}