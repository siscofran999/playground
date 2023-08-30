package com.sisco.playground.seekbar

import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.sisco.playground.databinding.ActivitySeekbarBinding
import java.text.NumberFormat
import java.util.Locale

class SeekbarActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeekbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekbarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val limit = 10000000
        val calculateLimit = limit/1000000
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        binding.valueLimit.text = formatRupiah.format(limit.toDouble())

        val minLimit = 1000000
        binding.minLimit.text = formatRupiah.format(minLimit.toDouble())
        binding.maxLimit.text = formatRupiah.format(limit.toDouble())

        val maxSeekbar = calculateLimit-1
        binding.seekBar.max = maxSeekbar
        binding.seekBar.progress = maxSeekbar
        Log.i("TAG", "onCreate: max -> $maxSeekbar")
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                Log.i("TAG", "onProgressChanged: $progress")
                if (progress > 0) {
                    val updateLimit = progress.plus(1) * minLimit
                    binding.valueLimit.text = formatRupiah.format(updateLimit.toDouble())
                }else {
                    binding.valueLimit.text = formatRupiah.format(minLimit.toDouble())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }
}