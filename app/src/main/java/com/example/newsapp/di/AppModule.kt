package com.example.newsapp.di

import com.example.newsapp.presentation.viewmodels.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel<NewsViewModel> {
        NewsViewModel(
            getNewsUseCase = get(),
            saveNewsUseCase = get (),
            deleteNewsUseCase = get (),
            getNewsFromService = get (),
            setVisibleNewsUseCase = get ()
        )
    }
}