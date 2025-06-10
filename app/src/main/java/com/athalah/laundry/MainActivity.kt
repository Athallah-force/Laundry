package com.athalah.laundry

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.athalah.laundry.cabang.Data_cabang
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import layanan.data_layanan_activity
import pegawai.data_pegawai_Activity
import pelanggan.data_pelanggan_Activity
import pelanggan.tambah_pelanggan_Activity
import tambahkan.Data_tambahkan
import java.util.Date
import java.util.Locale
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat




class MainActivity : AppCompatActivity() {
    lateinit var cv_layanan: CardView
    lateinit var cv_tambahan: CardView
    lateinit var cv_pegawai: CardView
    lateinit var cv_pelanggan: CardView
    lateinit var cv_transaksi: CardView
    lateinit var cv_laporan: CardView
    lateinit var cv_cabang: CardView
    lateinit var cv_akun: CardView
    lateinit var tvEstimasi: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val greetingText = findViewById<TextView>(R.id.greeting)
        val dateText = findViewById<TextView>(R.id.date)

        val greeting = getGreeting(this) // 'this' adalah context di MainActivity

        dateText.text = getCurrentDate()

        tvEstimasi = findViewById(R.id.amount_estimation)

        getTotalEstimasi()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cv_layanan = findViewById(R.id.card3)
        cv_tambahan = findViewById(R.id.card4)
        cv_pegawai = findViewById(R.id.card5)
        cv_pelanggan = findViewById(R.id.pelanggan)
        cv_transaksi = findViewById(R.id.transaksi)
        cv_laporan = findViewById(R.id.laporan)
        cv_cabang = findViewById(R.id.card6)
        cv_akun = findViewById(R.id.card2)

        cv_layanan.setOnClickListener {
            val intent = Intent(this, data_layanan_activity::class.java)
            startActivity(intent)
        }
        cv_akun.setOnClickListener {
            val intent = Intent(this, Akun::class.java)
            startActivity(intent)
        }
        cv_cabang.setOnClickListener {
            val intent = Intent(this, Data_cabang::class.java)
            startActivity(intent)
        }
        cv_tambahan.setOnClickListener {
            val intent = Intent(this, Data_tambahkan::class.java)
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
        cv_transaksi.setOnClickListener {
            val intent = Intent(this, Transaksi::class.java)
            startActivity(intent)
        }
        cv_laporan.setOnClickListener {
            val intent = Intent(this, activity_data_laporan::class.java)
            startActivity(intent)
        }


    }

    private fun getGreeting(context: Context): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 4..10 -> context.getString(R.string.greeting_morning)
            in 11..14 -> context.getString(R.string.greeting_afternoon)
            in 15..17 -> context.getString(R.string.greeting_evening)
            else -> context.getString(R.string.greeting_night)
        }
    }


    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getTotalEstimasi() {
        val dbRef = FirebaseDatabase.getInstance().getReference("laporan")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var total = 0L

                for (data in snapshot.children) {
                    val status = data.child("status").getValue(String::class.java)
                    val estimasiStr = data.child("estimasi").getValue(String::class.java) ?: "Rp 0"

                    if (status == "Lunas") {
                        // Parsing string "Rp 30.000,00" ke Long
                        val angka = estimasiStr
                            .replace("Rp", "")       // hapus Rp
                            .replace(".", "")        // hapus titik
                            .replace(",", "")        // hapus koma (opsional, biar tidak error)
                            .trim()

                        val estimasi = angka.toLongOrNull() ?: 0L
                        total += estimasi
                    }
                }

                val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(total)
                tvEstimasi.text = formatted
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("estimasi", "Gagal ambil data: ${error.message}")
                tvEstimasi.text = "Rp0"
            }
        })
    }







}