package com.fgr.githubuserfinder.utils

import androidx.room.TypeConverter
import com.fgr.githubuserfinder.response.ListUsers
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun isNotEmpty(list: List<*>?): Boolean {
    return list != null && list.isNotEmpty()
}

class ListUsersTypeConverter {

    private val gson = Gson()

}
