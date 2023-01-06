package com.example.newsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.domain.models.News


@Dao
interface NewsDao {

    @Query("SELECT * FROM news")
    fun getAllNews(): List<News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNews(news: List<News>)

}