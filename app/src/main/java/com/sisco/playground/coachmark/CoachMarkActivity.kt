package com.sisco.playground.coachmark

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sisco.playground.databinding.ActivityCoachMarkBinding

class CoachMarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoachMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoachMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val coachMarkSequence = CoachMarkSequence(this)
        coachMarkSequence.apply {
            addItem(
                binding.txvTop,
                "Rekening & Kartu Kredit",
                "Lihat informasi Akun Anda terkini melalui tab berikut:"
            )
            addItem(
                binding.txvStartTop,
                "Lihat Saldo & Rekening",
                "Cek saldo anda dan berbagi nomor rekening dengan pengguna lainnya"
            )
            addItem(
                binding.txvEndTop,
                "Atur Kartu Kredit",
                "Di sini Anda dapat mengatur kartu kredit sesuai dengan kebutuhan"
            )
            addItem(
                binding.txvEndBottom,
                "Atur Kartu Kredit",
                "Di sini Anda dapat mengatur kartu kredit sesuai dengan kebutuhan"
            )
            addItem(
                binding.txvStartBottom,
                "Atur Kartu Kredit",
                "Di sini Anda dapat mengatur kartu kredit sesuai dengan kebutuhan"
            )
            addItem(
                binding.txvBottom,
                "Atur Kartu Kredit",
                "Di sini Anda dapat mengatur kartu kredit sesuai dengan kebutuhan",
                "Siap Jelajah"
            )
            start(window?.decorView as ViewGroup)
            setOnFinishCallback {
                Toast.makeText(this@CoachMarkActivity, "Finish", Toast.LENGTH_SHORT).show()
            }
        }
    }
}