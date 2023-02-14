package com.example.newsapp.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.data.database.NewsDataBase
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.domain.usecase.DeleteNewsUseCase
import com.example.newsapp.domain.usecase.GetNewsUseCase
import com.example.newsapp.domain.usecase.SaveNewsUseCase
import com.example.newsapp.domain.usecase.SetVisibleNewsUseCase

class NewsViewModelFactory(context: Context): ViewModelProvider.Factory {

    private val newsRepository by lazy(LazyThreadSafetyMode.NONE) {
        val daoNews = NewsDataBase.getInstance(context).getNewsDao()
        NewsRepositoryImpl(daoNews)
    }

    private val getNewsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetNewsUseCase(newsRepository = newsRepository)
    }

    private val saveNewsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveNewsUseCase(newsRepository = newsRepository)
    }

    private val deleteNewsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        DeleteNewsUseCase(newsRepository = newsRepository)
    }

    private val setVisibleNewsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SetVisibleNewsUseCase(newsRepository = newsRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            getNewsUseCase = getNewsUseCase,
            saveNewsUseCase = saveNewsUseCase,
            deleteNewsUseCase = deleteNewsUseCase,
            setVisibleNewsUseCase = setVisibleNewsUseCase
        ) as T
    }

}