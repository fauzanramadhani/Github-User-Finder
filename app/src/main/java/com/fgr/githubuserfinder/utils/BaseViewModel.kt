package com.fgr.githubuserfinder.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fgr.githubuserfinder.data.DetailViewModel

class DetailActivityModelFactory constructor(val username: String, val event: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(username, event) as T
    }
}
