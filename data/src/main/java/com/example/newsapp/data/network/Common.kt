package com.example.newsapp.data.network

import com.example.newsapp.data.network.`interface`.NewsApi

object Common {
    private val BASE_URL = "https://ws-tszh-1c-test.vdgb-soft.ru/api/mobile/"

    fun newsApi(): NewsApi = RetrofitClient.getNews(BASE_URL).create(NewsApi::class.java)
}