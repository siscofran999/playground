package com.sisco.playground.coachmark

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.sisco.playground.R
import com.sisco.playground.databinding.ActivityCoachMarkBinding
import com.sisco.playground.databinding.ItemCoachmarkBinding


class CoachMarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoachMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoachMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val coachMarkSequence = CoachMarkSequence(this)
        coachMarkSequence.addItem(binding.txvTop)
        coachMarkSequence.addItem(binding.txvStartTop)
        coachMarkSequence.addItem(binding.txvEndTop)
        coachMarkSequence.addItem(binding.txvStart)
        coachMarkSequence.addItem(binding.txvEnd)
        coachMarkSequence.addItem(binding.txvEndBottom)
        coachMarkSequence.addItem(binding.txvStartBottom)
        coachMarkSequence.addItem(binding.txvBottom)
        coachMarkSequence.start(window?.decorView as ViewGroup)
    }
}