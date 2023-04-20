package com.fgr.githubuserfinder.retrofit

import com.fgr.githubuserfinder.response.DetailResponse
import com.fgr.githubuserfinder.response.ListUsers
import com.fgr.githubuserfinder.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    companion object {
        private const val GITHUB_TOKEN_AUTHORIZATION =
            "Bearer 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"
    }

    @GET("search/users")
    @Headers("Authorization: $GITHUB_TOKEN_AUTHORIZATION")
    fun getUsers(
        @Query("q") query: String
    ): Call<SearchResponse>

    @GET("/users/{username}")
    @Headers("Authorization: $GITHUB_TOKEN_AUTHORIZATION")
    fun getDetail(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("/users/{username}/followers")
    @Headers("Authorization: $GITHUB_TOKEN_AUTHORIZATION")
    fun getFollowersList(
        @Path("username") username: String
    ): Call<List<ListUsers>>

    @GET("/users/{username}/following")
    @Headers("Authorization: $GITHUB_TOKEN_AUTHORIZATION")
    fun getFollowingList(
        @Path("username") username: String
    ): Call<List<ListUsers>>
}