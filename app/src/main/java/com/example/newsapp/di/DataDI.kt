package com.example.newsapp.di

import com.example.newsapp.data.database.NewsDataBase
import com.example.newsapp.data.repository.NewsRepositoryImpl import com.example.newsapp.domain.repository.NewsRepository
import org.koin.dsl.module

val dataModule = module {
    single<NewsRepository> {
        NewsRepositoryImpl(dao = NewsDataBase.getInstance(context = get()))
    }
}