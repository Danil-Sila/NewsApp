package com.example.newsapp.di


import com.example.newsapp.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    factory<GetNewsUseCase> {
        GetNewsUseCase(newsRepository = get())
    }

    factory<SaveNewsUseCase> {
        SaveNewsUseCase(newsRepository = get())
    }

    factory<DeleteNewsUseCase> {
        DeleteNewsUseCase(newsRepository = get())
    }

    factory<GetNewsFromServiceUseCase> {
        GetNewsFromServiceUseCase(newsRepository = get())
    }

    factory<SetVisibleNewsUseCase> {
        SetVisibleNewsUseCase(newsRepository = get())
    }
}