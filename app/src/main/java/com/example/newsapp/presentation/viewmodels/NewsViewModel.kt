package com.example.newsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.database.models.News
import com.example.newsapp.domain.models.NewsData
import com.example.newsapp.domain.repository.LoadNewsFromServiceListener
import com.example.newsapp.domain.usecase.*
import kotlinx.coroutines.*

sealed class LoadingNewsServiceState {
    object DefaultState: LoadingNewsServiceState()
    object SendRequestGetNews: LoadingNewsServiceState()
    class NewsSucceedState(val news: List<News>): LoadingNewsServiceState()
    class ErrorState(val message: String): LoadingNewsServiceState()
    object NewsEmptyState: LoadingNewsServiceState()
}

sealed class NewsDatabaseState {
    object NewsEmptyState: NewsDatabaseState()
    object NewsSaveState: NewsDatabaseState()
    class NewsGetState(val news: List<News>): NewsDatabaseState()
}

class NewsViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val deleteNewsUseCase: DeleteNewsUseCase,
    private val setVisibleNewsUseCase: SetVisibleNewsUseCase,
    private val getNewsFromService: GetNewsFromServiceUseCase
): ViewModel(), LoadNewsFromServiceListener {
    private val newsDatabaseLiveMutable = MutableLiveData<NewsDatabaseState>()
    private val newsLoadingLiveMutable = MutableLiveData<LoadingNewsServiceState>(LoadingNewsServiceState.DefaultState)
    val resultGetNewsFromDB: LiveData<NewsDatabaseState> = newsDatabaseLiveMutable
    val resultLoadingNews: LiveData<LoadingNewsServiceState> = newsLoadingLiveMutable

    fun getNews(modeHideNews: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            val news = getNewsUseCase.execute(modeHideNews)
            launch(Dispatchers.Main) {
                if (news.isNullOrEmpty()) {
                    newsDatabaseLiveMutable.value = NewsDatabaseState.NewsEmptyState
                } else {
                    newsDatabaseLiveMutable.value = NewsDatabaseState.NewsGetState(news = mapNewsList(newsListData = news))
                }
            }
        }
    }

    fun saveNews(news: List<News>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveNewsUseCase.execute(news = mapNewsListToDomain(newsList = news))
            launch(Dispatchers.Main) {
                newsDatabaseLiveMutable.value = NewsDatabaseState.NewsSaveState
            }
        }
    }

    fun deleteNews(news: News, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNewsUseCase.execute(news = mapNewsToDomain(news), onSuccess = onSuccess)
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun setVisibleNews(news: News, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setVisibleNewsUseCase.execute(news = mapNewsToDomain(news), onSuccess = onSuccess)
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun getNewsFromService() {
        newsLoadingLiveMutable.value = LoadingNewsServiceState.SendRequestGetNews
        getNewsFromService.execute(newsFromServiceState = this)
    }

    private fun mapNewsToDomain(news: News): NewsData {
        return NewsData(
            id = news.id,
            title = news.title,
            img = news.img,
            news_date = news.news_date,
            annotation = news.annotation,
            id_resourse = news.id_resourse,
            type = news.type,
            news_date_uts = news.news_date_uts,
            mobile_url = news.mobile_url,
            hide_news = news.hide_news
        )
    }

    private fun mapNewsListToDomain(newsList: List<News>): List<NewsData> {
        val newsListDomain = mutableListOf<NewsData>()
        for (news in newsList) {
            newsListDomain.add(
                NewsData(
                    id = news.id,
                    title = news.title,
                    img = news.img,
                    news_date = news.news_date,
                    annotation = news.annotation,
                    id_resourse = news.id_resourse,
                    type = news.type,
                    news_date_uts = news.news_date_uts,
                    mobile_url = news.mobile_url,
                    hide_news = news.hide_news
                )
            )
        }
        return newsListDomain
    }

    private fun mapNewsList(newsListData: List<NewsData>): List<News> {
        val newsList = mutableListOf<News>()
        for (news in newsListData) {
            newsList.add(
               News(
                    id = news.id,
                    title = news.title,
                    img = news.img,
                    news_date = news.news_date,
                    annotation = news.annotation,
                    id_resourse = news.id_resourse,
                    type = news.type,
                    news_date_uts = news.news_date_uts,
                    mobile_url = news.mobile_url,
                    hide_news = news.hide_news
                )
            )
        }
        return newsList
    }

    override fun idSuccess(news: List<NewsData>) {
        if (news.isEmpty()) {
            newsLoadingLiveMutable.value = LoadingNewsServiceState.NewsEmptyState
        } else {
            newsLoadingLiveMutable.value = LoadingNewsServiceState.NewsSucceedState(news = mapNewsList(newsListData = news))
        }
    }

    override fun isError(message: String) {
        newsLoadingLiveMutable.value = LoadingNewsServiceState.ErrorState(message = message)
    }

}