package com.example.newsapp.domain.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class News(
    @PrimaryKey
    var id: String,
    @ColumnInfo
    var title: String?,
    @ColumnInfo
    var img: String?,
    @ColumnInfo
    var news_date: String?,
    @ColumnInfo
    var annotation: String?,
    @ColumnInfo
    var id_resourse: String?,
    @ColumnInfo
    var type: String?,
    @ColumnInfo
    var news_date_uts: String?,
    @ColumnInfo
    var mobile_url: String?
): Parcelable
