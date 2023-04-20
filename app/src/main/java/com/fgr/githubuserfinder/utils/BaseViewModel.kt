package com.fgr.githubuserfinder.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fgr.githubuserfinder.di.Injection
import com.fgr.githubuserfinder.local.datastore.SettingPreferences
import com.fgr.githubuserfinder.repo.UsersRepository
import com.fgr.githubuserfinder.viewmodel.DetailViewModel
import com.fgr.githubuserfinder.viewmodel.HomeViewModel

class ViewModelFactory private constructor(private val usersRepository: UsersRepository) :
    ViewModelProvider.NewInstanceFactory() {

    private var username: String? = null

    fun setUsername(username: String) {
        this.username = username
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(requireNotNull(username), usersRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}
class HomeViewModelFactory private constructor(private val usersRepository: UsersRepository, private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(usersRepository, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(context: Context, dataStore: DataStore<Preferences>): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HomeViewModelFactory(Injection.provideRepository(context), Injection.provideThemeSetting(dataStore))
            }.also { instance = it }
    }
}
