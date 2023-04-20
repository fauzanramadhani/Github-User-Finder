package com.fgr.githubuserfinder.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fgr.githubuserfinder.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserFavDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserFavDatabase? = null
        fun getInstance(context: Context): UserFavDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserFavDatabase::class.java, "User.db"
                ).build()
            }
    }
}