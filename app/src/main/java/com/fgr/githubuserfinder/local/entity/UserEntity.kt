package com.fgr.githubuserfinder.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_user")
class UserEntity(

    @field:ColumnInfo(name = "username")
    @field:PrimaryKey
    val username: String,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "id")
    val id: Int,

    @field:ColumnInfo(name = "image_url")
    val imageUrl: String,

    @field:ColumnInfo(name = "followers_count")
    val followersCount: Int,

    @field:ColumnInfo(name = "following_count")
    val followingCount: Int,

    @field:ColumnInfo(name = "profile_url")
    val profile_url: String,
)