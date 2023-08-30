package com.sisco.playground.coachmark

import android.os.Bundle
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
        val coachMarkBuilder = CoachMarkOverlay.Builder(this)
            .setOverlayTargetView(binding.txvStart)

        coachMarkSequence.addItem(coachMarkBuilder, true)
        coachMarkSequence.start(window?.decorView as ViewGroup)
    }
}