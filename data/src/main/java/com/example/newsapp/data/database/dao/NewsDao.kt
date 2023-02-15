package com.example.newsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.data.database.models.News


@Dao
interface NewsDao {

    @Query("SELECT * FROM news WHERE hide_news = :modeNewsHide ORDER BY news_date DESC")
    fun getAllNews(modeNewsHide: Int): List<News>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveNews(news: List<News>)

    @Delete
    suspend fun deleteNews(news: News)

    @Update
    suspend fun setVisible(news: News)

}