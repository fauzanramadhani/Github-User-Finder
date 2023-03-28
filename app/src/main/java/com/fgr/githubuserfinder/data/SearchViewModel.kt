package com.fgr.githubuserfinder.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgr.githubuserfinder.response.ListUsers
import com.fgr.githubuserfinder.response.SearchResponse
import com.fgr.githubuserfinder.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private var _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private var _listUsers = MutableLiveData<List<ListUsers>>()
    val listUsers: LiveData<List<ListUsers>> = _listUsers


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun searchUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService("https://api.github.com/").getUsers(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body()?.items
                } else {
                    Log.e("isFailed Get User", " ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("onFailure Get User", "onFailure: ${t.message.toString()}")
            }
        })
    }
}