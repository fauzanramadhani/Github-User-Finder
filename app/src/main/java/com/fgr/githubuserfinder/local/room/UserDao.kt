package com.fgr.githubuserfinder.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fgr.githubuserfinder.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM fav_user")
    fun getAll(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFav(User: UserEntity)

    @Query("DELETE FROM fav_user WHERE username = :username")
    suspend fun deleteFav(username: String)

    @Query("SELECT EXISTS(SELECT * FROM fav_user WHERE username = :username)")
    suspend fun isFavorite(username: String) : Boolean
}