package com.example.newsapp.domain.models

data class NewsData (
    var id: String,
    var title: String?,
    var img: String?,
    var news_date: String?,
    var annotation: String?,
    var id_resourse: String?,
    var type: String?,
    var news_date_uts: String?,
    var mobile_url: String?,
    var hide_news: Boolean = false
)