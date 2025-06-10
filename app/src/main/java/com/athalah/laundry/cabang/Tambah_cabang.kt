package com.athalah.laundry.cabang

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_cabang
import com.google.firebase.database.FirebaseDatabase

class Tambah_cabang : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myref = database.getReference("cabang")
    lateinit var et_nama_cabang: EditText
    lateinit var et_alamat_cabang: EditText
    lateinit var bt_simpan_cabang: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseDatabase.getInstance().reference.child("test")
            .setValue("cek koneksi")
            .addOnCompleteListener {
                Log.d("FirebaseTest", "Status: ${it.isSuccessful}")
            }
            .addOnFailureListener {
                Log.e("FirebaseTest", "Gagal konek Firebase", it)
            }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_cabang)
        init()
        bt_simpan_cabang.setOnClickListener{
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        et_nama_cabang = findViewById(R.id.et_nama_cabang)
        et_alamat_cabang = findViewById(R.id.et_alamat)
        bt_simpan_cabang = findViewById(R.id.bt_simpan_cabang)
    }
    fun cekValidasi() {
        val nama_cabang = et_nama_cabang.text.toString()
        val alamat = et_alamat_cabang.text.toString()

        if(nama_cabang.isEmpty()) {
            et_nama_cabang.error = this.getString(R.string.validasi_nama_cabang)
            Toast.makeText(
                this@Tambah_cabang,
                this.getString(R.string.validasi_nama_cabang),
                Toast.LENGTH_SHORT
            ).show()
            et_nama_cabang.requestFocus()
            return
        }
        if(alamat.isEmpty()) {
            et_alamat_cabang.error = this.getString(R.string.validasi_alamat_cabang)
            Toast.makeText(
                this@Tambah_cabang,
                this.getString(R.string.validasi_alamat_cabang),
                Toast.LENGTH_SHORT
            ).show()
            et_alamat_cabang.requestFocus()
            return
        }
        simpan()
    }
    fun simpan () {
        val cabangBaru = myref.push()
        val cabangId = cabangBaru.key ?: return
        val data = model_cabang(
            idCabang = cabangId,
            namaCabang = et_nama_cabang.text.toString(),
            alamat = et_alamat_cabang.text.toString()

        )

        Log.d("TambahCabang", "Mulai simpan: $data")
        cabangBaru.setValue(data)
            .addOnSuccessListener {
                Log.d("TambahCabang", "Berhasil simpan")
                Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("cabang_id", cabangId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Log.e("TambahCabang", "Gagal simpan")
                Toast.makeText(this, "Gagal Simpan Data", Toast.LENGTH_SHORT).show()
            }
            .addOnCompleteListener {
                Log.d("TambahCabang", "SetValue Complete: ${it.isSuccessful}")
            }
    }
}