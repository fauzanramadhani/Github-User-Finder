package com.fgr.githubuserfinder.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fgr.githubuserfinder.ui.DetailUserRvFragment

class DetailPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = DetailUserRvFragment()
                fragment.arguments = Bundle().apply {
                    putInt(DetailUserRvFragment.INDEX_POSITION, position)
                    putString(DetailUserRvFragment.USERNAME, username)
                }
            }
            1 -> {
                fragment = DetailUserRvFragment()
                fragment.arguments = Bundle().apply {
                    putInt(DetailUserRvFragment.INDEX_POSITION, position)
                    putString(DetailUserRvFragment.USERNAME, username)
                }
            }
        }
        return fragment as Fragment
    }
}