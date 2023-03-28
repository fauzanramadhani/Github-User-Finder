package com.fgr.githubuserfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fgr.githubuserfinder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }
}