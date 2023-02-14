package com.example.newsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.domain.models.News
import com.example.newsapp.domain.usecase.DeleteNewsUseCase
import com.example.newsapp.domain.usecase.GetNewsUseCase
import com.example.newsapp.domain.usecase.SaveNewsUseCase
import com.example.newsapp.domain.usecase.SetVisibleNewsUseCase
import com.example.newsapp.network.Common
import kotlinx.coroutines.*

sealed class LoadingNewsState {
    object DefaultState: LoadingNewsState()
    object SendRequestGetNews: LoadingNewsState()
    class NewsSucceedState(val news: List<News>): LoadingNewsState()
    class ErrorState(val message: String): LoadingNewsState()
    object NewsEmptyState: LoadingNewsState()
}

sealed class GetNewsStateFromDB {
    object NewsEmptyState: GetNewsStateFromDB()
    class NewsGetState(val news: List<News>): GetNewsStateFromDB()
}

class NewsViewModel(
    private val getNewsUseCase: GetNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val deleteNewsUseCase: DeleteNewsUseCase,
    private val setVisibleNewsUseCase: SetVisibleNewsUseCase
): ViewModel() {
    private val newsLiveMutable = MutableLiveData<GetNewsStateFromDB>()
    private val newsLoadingLiveMutable = MutableLiveData<LoadingNewsState>(LoadingNewsState.DefaultState)
    val resultGetNewsFromDB: LiveData<GetNewsStateFromDB> = newsLiveMutable
    val resultLoadingNews: LiveData<LoadingNewsState> = newsLoadingLiveMutable

    fun getNews(modeHideNews: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            val news = getNewsUseCase.execute(modeHideNews)
            launch(Dispatchers.Main) {
                if (news.isNullOrEmpty()) {
                    newsLiveMutable.value = GetNewsStateFromDB.NewsEmptyState
                } else {
                    newsLiveMutable.value = GetNewsStateFromDB.NewsGetState(news = news)
                }
            }
        }
    }

    fun saveNews(news: List<News>) {
        viewModelScope.launch(Dispatchers.IO) {
            saveNewsUseCase.execute(news = news)
        }
    }

    fun deleteNews(news: News, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteNewsUseCase.execute(news, onSuccess = onSuccess)
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun setVisibleNews(news: News, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setVisibleNewsUseCase.execute(news = news, onSuccess = onSuccess)
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun getNewsFromService() {
        newsLoadingLiveMutable.value = LoadingNewsState.SendRequestGetNews
        CoroutineScope(Dispatchers.IO).async {
            val response = Common.newsApi().getNewsList()
            if (response.isSuccessful) {
                if (response.body()?.data?.news.isNullOrEmpty()) {
                    launch(Dispatchers.Main) {
                        newsLoadingLiveMutable.value = LoadingNewsState.NewsEmptyState
                    }
                } else {
                    launch(Dispatchers.Main) {
                        val news = response.body()?.data?.news
                        newsLoadingLiveMutable.value = LoadingNewsState.NewsSucceedState(news = news!!)
                    }
                }
            } else {
                launch(Dispatchers.Main) {
                    newsLoadingLiveMutable.value = LoadingNewsState.ErrorState(message = response.body()?.error?.message.toString())
                }
            }
        }
    }
}