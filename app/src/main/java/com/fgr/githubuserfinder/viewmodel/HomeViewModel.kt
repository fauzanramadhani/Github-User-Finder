package com.fgr.githubuserfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fgr.githubuserfinder.local.datastore.SettingPreferences
import com.fgr.githubuserfinder.local.entity.UserEntity
import com.fgr.githubuserfinder.repo.UsersRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    usersRepository: UsersRepository,
    private val pref: SettingPreferences
) : ViewModel() {
    val listUsers: LiveData<List<UserEntity>> = usersRepository.getFavList()

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}