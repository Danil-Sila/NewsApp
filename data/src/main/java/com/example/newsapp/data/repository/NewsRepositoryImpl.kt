package com.example.newsapp.data.repository

import com.example.newsapp.data.database.NewsDataBase
import com.example.newsapp.data.database.models.News
import com.example.newsapp.data.network.Common
import com.example.newsapp.domain.models.NewsData
import com.example.newsapp.domain.repository.LoadNewsFromServiceListener
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.*

class NewsRepositoryImpl(dao: NewsDataBase): NewsRepository {

    private val newsDao = dao.getNewsDao()

    override fun getNews(modeNewsHide: Boolean): List<NewsData> {
       return mapNewsListToDomain(newsList = newsDao.getAllNews(getNewsMode(modeNewsHide)))
    }

    private fun getNewsMode(modeNewsHide: Boolean): Int {
        return if (modeNewsHide) {
            1
        } else {
            0
        }
    }

    override fun saveNews(news: List<NewsData>) {
        newsDao.saveNews(news = mapNewsList(newsListData = news))
    }

    override suspend fun deleteNews(news: NewsData, onSuccess: () -> Unit) {
        newsDao.deleteNews(news = mapNews(news = news))
    }

    override suspend fun setVisibleNews(news: NewsData, onSuccess: () -> Unit) {
        newsDao.setVisible(news = mapNews(news = news))
    }

    override fun getNewsFromService(newsFromServiceState: LoadNewsFromServiceListener) {
        CoroutineScope(Dispatchers.IO).async {
            val response = Common.newsApi().getNewsList()
            if (response.isSuccessful) {
                launch(Dispatchers.Main) {
                    val news = response.body()?.data?.news
                    newsFromServiceState.idSuccess(news = news!!)
                }
            } else {
                launch(Dispatchers.Main) {
                    newsFromServiceState.isError(message = response.body()?.error?.message.toString())
                }
            }
        }

    }

    private fun mapNews(news: NewsData): News {
        return News(id = news.id, title = news.title, img = news.img, news_date = news.news_date,
            annotation = news.annotation, id_resourse = news.id_resourse, type = news.type,
            news_date_uts = news.news_date_uts, mobile_url = news.mobile_url, hide_news = news.hide_news)
    }

    private fun mapNewsList(newsListData: List<NewsData>): List<News> {
        val newsList = mutableListOf<News>()
        for (news in newsListData) {
            newsList.add(
                News(id = news.id, title = news.title, img = news.img, news_date = news.news_date,
                annotation = news.annotation, id_resourse = news.id_resourse, type = news.type,
                news_date_uts = news.news_date_uts, mobile_url = news.mobile_url, hide_news = news.hide_news)
            )
        }
        return newsList
    }

    private fun mapNewsListToDomain(newsList: List<News>): List<NewsData> {
        val newsListDomain = mutableListOf<NewsData>()
        for (news in newsList) {
            newsListDomain.add(NewsData(id = news.id, title = news.title, img = news.img, news_date = news.news_date,
                annotation = news.annotation, id_resourse = news.id_resourse, type = news.type,
                news_date_uts = news.news_date_uts, mobile_url = news.mobile_url, hide_news = news.hide_news)
            )
        }
        return newsListDomain
    }
}