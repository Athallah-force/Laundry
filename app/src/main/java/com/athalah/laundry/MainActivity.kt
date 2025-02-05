package com.athalah.laundry

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pelanggan.Activity_layanan_pelanggan


class MainActivity : AppCompatActivity() {
    lateinit var cv_layanan: CardView
    lateinit var cv_tambahan: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cv_layanan = findViewById(R.id.card3)
        cv_tambahan = findViewById(R.id.card4)

        cv_layanan.setOnClickListener {
            val intent = Intent(this, Activity_layanan_pelanggan::class.java)
            startActivity(intent)
        }

        cv_tambahan.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan_Activity::class.java)
            startActivity(intent)
        }
    }
}