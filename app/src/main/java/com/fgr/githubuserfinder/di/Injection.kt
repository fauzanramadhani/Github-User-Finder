package com.fgr.githubuserfinder.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fgr.githubuserfinder.local.datastore.SettingPreferences
import com.fgr.githubuserfinder.local.room.UserFavDatabase
import com.fgr.githubuserfinder.repo.UsersRepository

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val database = UserFavDatabase.getInstance(context)
        val dao = database.userDao()
        return UsersRepository.getInstance(userDao = dao)
    }
    fun provideThemeSetting(dataStore: DataStore<Preferences>): SettingPreferences {
        return SettingPreferences.getInstance(dataStore)
    }
}