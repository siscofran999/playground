package com.sisco.playground.motionLayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sisco.playground.R
import com.sisco.playground.databinding.ActivityMotionLayoutBinding

class MotionLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMotionLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMotionLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}