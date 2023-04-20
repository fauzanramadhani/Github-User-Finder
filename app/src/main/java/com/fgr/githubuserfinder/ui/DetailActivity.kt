package com.fgr.githubuserfinder.ui

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.fgr.githubuserfinder.R
import com.fgr.githubuserfinder.adapter.DetailPagerAdapter
import com.fgr.githubuserfinder.databinding.ActivityDetailBinding
import com.fgr.githubuserfinder.local.entity.UserEntity
import com.fgr.githubuserfinder.response.ListUsers
import com.fgr.githubuserfinder.utils.ViewModelFactory
import com.fgr.githubuserfinder.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "e_id"
        const val EXTRA_USERNAME = "e_username"
        const val EXTRA_IMAGE_URL = "e_image_url"
        const val EXTRA_PROFILE_URL = "e_url_profile_url"
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

        val myId = intent.getIntExtra(EXTRA_ID, 0)
        var name = "Empty"
        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        var followersCount = 0
        var followingCount = 0
        val followerList = mutableListOf<ListUsers>()
        val followingList = mutableListOf<ListUsers>()
        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        val profileUrl = intent.getStringExtra(EXTRA_PROFILE_URL) ?: ""

        setupMenu(username, profileUrl)


        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        factory.setUsername(username)
        val detailViewModel: DetailViewModel by viewModels {
            factory
        }

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
                name = username
            } else {
                binding.userDetailName.text = it.name
                name = it.name
                followersCount = it.followers
                followingCount = it.following
            }
        }

        val typedValue = TypedValue()
        theme.resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true)
        val color = ContextCompat.getColor(this, typedValue.resourceId)

        detailViewModel.followerList.observe(this) {
            if (it.isNullOrEmpty()) {
                Log.e("DetailActivity", "Failed Get Follower List Detail")
            } else {
                followerList.addAll(it)
            }
        }

        detailViewModel.followingList.observe(this) {
            if (it.isNullOrEmpty()) {
                Log.e("DetailActivity", "Failed Get Following List Detail")
            } else {
                followingList.addAll(it)
            }
        }

        detailViewModel.isFavorite.observe(this) { isFavorite ->
            if (!isFavorite) {
                val drawable = ContextCompat.getDrawable(this, R.drawable.ic_not_fav)
                drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                binding.fabFav.setImageDrawable(drawable)
                binding.fabFav.setOnClickListener {
                    detailViewModel.addFav(
                        UserEntity(
                            id = myId,
                            username = username,
                            name = name,
                            imageUrl = imageUrl,
                            followersCount = followersCount,
                            followingCount = followingCount,
                            profile_url = profileUrl
                        )
                    )
                }
            } else {
                val drawable = ContextCompat.getDrawable(this, R.drawable.ic_fav)
                drawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                binding.fabFav.setImageDrawable(drawable)
                binding.fabFav.setOnClickListener {
                    detailViewModel.deleteFav(username)
                }
            }
            detailViewModel.isFav()
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

    private fun setupMenu(username: String, url: String) {
        this@DetailActivity.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.share -> {
                        Log.e("DetailActivity", "Share!")
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Profil Github $username")
                        intent.putExtra(Intent.EXTRA_TEXT, "Mari kita terhubung di GitHub: $url")
                        startActivity(Intent.createChooser(intent, "Bagikan profil Github melalui:"))
                        true
                    }
                    else -> false
                }
            }

        })
    }
}