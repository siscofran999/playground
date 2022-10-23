package com.sisco.playground

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sisco.playground.biometric.BiometricActivity
import com.sisco.playground.coachmark.CoachMarkActivity
import com.sisco.playground.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBiometric.setOnClickListener {
                startActivity(Intent(this@MainActivity, BiometricActivity::class.java))
            }
            btnCoachMark.setOnClickListener {
                startActivity(Intent(this@MainActivity, CoachMarkActivity::class.java))
            }
        }
    }
}