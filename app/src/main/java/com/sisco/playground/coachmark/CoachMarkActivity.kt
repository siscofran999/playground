package com.sisco.playground.coachmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sisco.playground.databinding.ActivityCoachMarkBinding

class CoachMarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoachMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoachMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoachMarkView.Builder(this).setTargetView(binding.testEnd).build()
    }
}