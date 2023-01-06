package com.example.newsapp.network.`interface`

import com.example.newsapp.domain.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {
    @GET("news/list/")
    suspend fun getNewsList(): Response<NewsResponse>
}