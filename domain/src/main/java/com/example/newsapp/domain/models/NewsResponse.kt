package com.example.newsapp.domain.models


data class NewsResponse(
    var success: Boolean,
    var data: NewsArray,
    var error: Error
)

data class NewsArray(
    var news: List<NewsData>
)

data class Error(
    var code: String,
    var message: String
)