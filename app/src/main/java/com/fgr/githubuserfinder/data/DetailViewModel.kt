package com.fgr.githubuserfinder.data

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fgr.githubuserfinder.response.DetailResponse
import com.fgr.githubuserfinder.response.ListUsers
import com.fgr.githubuserfinder.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel @ViewModelInject constructor(
    username: String,
    event: Int
) : ViewModel() {
    private val _followerList = MutableLiveData<List<ListUsers>>()
    val followerList: LiveData<List<ListUsers>> = _followerList
    private val _followingList = MutableLiveData<List<ListUsers>>()
    val followingList: LiveData<List<ListUsers>> = _followingList
    private val _detailList = MutableLiveData<DetailResponse>()
    val detailList: LiveData<DetailResponse> = _detailList
    private val _isLoadingFollowers = MutableLiveData<Boolean>()
    val isLoadingFollowers: LiveData<Boolean> = _isLoadingFollowers
    private val _isLoadingFollowing = MutableLiveData<Boolean>()
    val isLoadingFollowing: LiveData<Boolean> = _isLoadingFollowing
    private val _isLoadingUserDetail = MutableLiveData<Boolean>()
    val isLoadingUserDetail: LiveData<Boolean> = _isLoadingUserDetail

    private fun getUserDetail(username: String) {
        _isLoadingUserDetail.value = true
        val client = ApiConfig.getApiService("https://api.github.com").getDetail(username)
        client.enqueue(object: Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoadingUserDetail.value = false
                if (response.isSuccessful) {
                    _detailList.value = response.body()
                } else {
                    Log.e("isFailed Get Detail Info", " ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoadingFollowers.value = false
                TODO("Not yet implemented")
            }

        })
    }

    private fun getUserFollowers(username: String) {
        _isLoadingFollowers.value = true
        val client = ApiConfig.getApiService("https://api.github.com").getFollowersList(username)
        client.enqueue(object : Callback<List<ListUsers>> {
            override fun onResponse(
                call: Call<List<ListUsers>>,
                response: Response<List<ListUsers>>
            ) {
                _isLoadingFollowers.value = false
                if (response.isSuccessful) {
                    _followerList.value = response.body()
                } else {
                    Log.e("isFailed Get Follower Count", " ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUsers>>, t: Throwable) {
                _isLoadingFollowers.value = false
                Log.e("Failed Get Follower Count", t.message.toString())
            }
        })
    }

    private fun getUserFollowing(username: String) {
        _isLoadingFollowing.value = true
        val client = ApiConfig.getApiService("https://api.github.com").getFollowingList(username)
        client.enqueue(object : Callback<List<ListUsers>> {
            override fun onResponse(
                call: Call<List<ListUsers>>,
                response: Response<List<ListUsers>>
            ) {
                _isLoadingFollowing.value = false
                if (response.isSuccessful) {
                    _followingList.value = response.body()
                } else {
                    Log.e("isFailed Get Following Count", " ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListUsers>>, t: Throwable) {
                _isLoadingFollowing.value = false
                Log.e("Failed Get Following Count", t.message.toString())
            }
        })
    }

    init {
        when (event) {
            0 -> {
                getUserFollowers(username)
            }
            1 -> {
                getUserFollowing(username)
            }
            else -> {
                getUserDetail(username)
            }
        }

    }
}