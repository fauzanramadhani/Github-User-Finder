package com.fgr.githubuserfinder.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fgr.githubuserfinder.R
import com.fgr.githubuserfinder.adapter.DetailPagerAdapter
import com.fgr.githubuserfinder.data.DetailViewModel
import com.fgr.githubuserfinder.databinding.ActivityDetailBinding
import com.fgr.githubuserfinder.utils.DetailActivityModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "e_username"
        const val EXTRA_IMAGE_URL = "e_image_url"
    }

    private lateinit var binding: ActivityDetailBinding
    private val tabTitle = arrayListOf(
        R.string.followers,
        R.string.following
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""

        val factory = DetailActivityModelFactory(username, 2)
        val detailViewModel = ViewModelProvider(
            this,
            factory
        )[DetailViewModel::class.java]
        showProgressBar(detailViewModel)


        Glide.with(this)
            .load(imageUrl)
            .into(binding.userDetailImage)

        detailViewModel.detailList.observe(this) {
            binding.userDetailId.text = resources.getString(R.string.my_id, it.id.toString())
            binding.userDetailFollowers.text = it.followers.toString()
            binding.userDetailFollowing.text = it.following.toString()
            if (it.name.isNullOrBlank()) {
                binding.userDetailName.text = username
            } else {
                binding.userDetailName.text = it.name
            }
        }

        val detailUserFollPagerAdapter = DetailPagerAdapter(this)
        detailUserFollPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = detailUserFollPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(tabTitle[position])
        }.attach()

        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            title = username
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showProgressBar(detailViewModel: DetailViewModel) {
        detailViewModel.isLoadingUserDetail.observe(this) { isLoadingDetail ->
            if (isLoadingDetail) {
                binding.userDetailInfoProgressBar.visibility = View.VISIBLE
                binding.userDetailName.visibility = View.GONE
                binding.userDetailId.visibility = View.GONE
                binding.userDetailFollowers.visibility = View.GONE
                binding.userDetailFollowing.visibility = View.GONE
                Log.e("Loading", isLoadingDetail.toString())
            } else {
                binding.userDetailInfoProgressBar.visibility = View.GONE
                binding.userDetailName.visibility = View.VISIBLE
                binding.userDetailId.visibility = View.VISIBLE
                binding.userDetailFollowers.visibility = View.VISIBLE
                binding.userDetailFollowing.visibility = View.VISIBLE
                Log.e("Loading", isLoadingDetail.toString())
            }
        }
    }
}