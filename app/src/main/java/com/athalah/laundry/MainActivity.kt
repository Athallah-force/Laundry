package com.athalah.laundry

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import layanan.data_layanan_activity
import pegawai.data_pegawai_Activity
import pelanggan.data_pelanggan_Activity
import pelanggan.tambah_pelanggan_Activity


class MainActivity : AppCompatActivity() {
    lateinit var cv_layanan: CardView
    lateinit var cv_tambahan: CardView
    lateinit var cv_pegawai: CardView
    lateinit var cv_pelanggan: CardView

    @SuppressLint("MissingInflatedId")
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
        cv_pegawai = findViewById(R.id.card5)
        cv_pelanggan = findViewById(R.id.pelanggan)

        cv_layanan.setOnClickListener {
            val intent = Intent(this, data_layanan_activity::class.java)
            startActivity(intent)
        }

        cv_tambahan.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan_Activity::class.java)
            startActivity(intent)
        }
        cv_pegawai.setOnClickListener {
            val intent = Intent(this, data_pegawai_Activity::class.java)
            startActivity(intent)
        }
        cv_pelanggan.setOnClickListener {
            val intent = Intent(this, data_pelanggan_Activity::class.java)
            startActivity(intent)
        }
    }
}