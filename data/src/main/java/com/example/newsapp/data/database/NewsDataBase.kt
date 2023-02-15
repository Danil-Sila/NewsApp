package com.example.newsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newsapp.data.database.dao.NewsDao
import com.example.newsapp.data.database.models.News

@Database(entities = [News::class], version = 2, exportSchema = true)
abstract class NewsDataBase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao

    companion object {
        private var database: NewsDataBase?= null
        @Synchronized
        fun getInstance(context: Context): NewsDataBase {
            return if (database == null) {
                database = Room.databaseBuilder(context, NewsDataBase::class.java, "db")
                    .addMigrations(MIGRATION_1_2)
                    .build()
                database as NewsDataBase
            } else {
                database as NewsDataBase
            }
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE News ADD hide_news INTEGER NOT NULL DEFAULT(0) ")
            }
        }
    }
}