package com.fgr.githubuserfinder.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.fgr.githubuserfinder.local.entity.UserEntity
import com.fgr.githubuserfinder.local.room.UserDao

class UsersRepository private constructor(
    private val userDao: UserDao,
){

    fun getFavList(): LiveData<List<UserEntity>> = userDao.getAll()

    suspend fun isFav(username: String) = userDao.isFavorite(username)

    suspend fun addFav(user: UserEntity) {
        if (!isFav(user.username)) {
            userDao.addFav(user)
        } else {
            Log.e("UsersRepository", "User Already in Favorite!")
        }
    }

    suspend fun deleteFav(username: String) {
        if (isFav(username)) {
            userDao.deleteFav(username)
        } else {
            Log.e("UsersRepository", "User is Not in Favorite!")
        }
    }


    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            userDao: UserDao,
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(userDao)
            }.also { instance = it }
    }
}