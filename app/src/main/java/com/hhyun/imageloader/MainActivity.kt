package com.hhyun.imageloader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hhyun.imageloader.databinding.ActivityMainBinding
import com.hhyun.imageloader.test.ImageListActivity
import com.hhyun.imageloader.test.ImageLoaderActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
    }

    private fun setListener() {

        binding.buttonTestImageLoader.setOnClickListener {
            startActivity(Intent(this, ImageLoaderActivity::class.java))
        }

        binding.buttonTestImageList.setOnClickListener {
            startActivity(Intent(this, ImageListActivity::class.java))
        }

    }
}