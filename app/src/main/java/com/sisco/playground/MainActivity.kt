package com.sisco.playground

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sisco.playground.biometric.BiometricActivity
import com.sisco.playground.coachmark.CoachMarkActivity
import com.sisco.playground.compose.LoginActivity
import com.sisco.playground.databinding.ActivityMainBinding
import com.sisco.playground.module.openProfileActivity
import com.sisco.playground.motionLayout.MotionLayoutActivity
import com.sisco.playground.seekbar.SeekbarActivity

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
            btnLogin.setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
            btnProfile.setOnClickListener {
                openProfileActivity(this@MainActivity)
            }
            btnMotionLayout.setOnClickListener {
                startActivity(Intent(this@MainActivity, MotionLayoutActivity::class.java))
            }
            btnCoachMark.setOnClickListener {
                startActivity(Intent(this@MainActivity, CoachMarkActivity::class.java))
            }
            btnSeekbar.setOnClickListener {
                startActivity(Intent(this@MainActivity, SeekbarActivity::class.java))
            }
        }

    }
}