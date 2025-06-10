package com.athalah.laundry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.athalah.laundry.databinding.ActivityDataLaporanBinding
import com.athalah.laundry.model_data.model_laporan
import com.athalah.laundry.adapter.adapter_data_laporan
import com.google.firebase.database.*

class activity_data_laporan : AppCompatActivity() {

    private lateinit var binding: ActivityDataLaporanBinding
    private lateinit var database: DatabaseReference
    private lateinit var laporanList: MutableList<model_laporan>
    private lateinit var laporanAdapter: adapter_data_laporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("laporan")
        laporanList = mutableListOf()
        laporanAdapter = adapter_data_laporan(this, laporanList)

        binding.rvDataLaporan.apply {
            layoutManager = LinearLayoutManager(this@activity_data_laporan)
            adapter = laporanAdapter
        }

        // Menambahkan listener pencarian
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                laporanAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        ambilDataLaporan()
        // updateSemuaStatusJikaKosong() // Opsional: aktifkan jika perlu memperbaiki data lama
    }

    private fun ambilDataLaporan() {
        binding.progressBar.visibility = View.VISIBLE
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                laporanList.clear()
                for (data in snapshot.children) {
                    val laporan = data.getValue(model_laporan::class.java)
                    if (laporan != null) {
                        laporanList.add(laporan)
                    }
                }
                laporanAdapter = adapter_data_laporan(this@activity_data_laporan, laporanList)
                binding.rvDataLaporan.adapter = laporanAdapter

                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@activity_data_laporan, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Opsional: panggil sekali untuk perbaiki data lama
    private fun updateSemuaStatusJikaKosong() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val laporan = data.getValue(model_laporan::class.java)
                    if (laporan != null) {
                        val id = data.key ?: continue
                        if (laporan.status.isNullOrEmpty()) {
                            val statusBaru = if (laporan.metodePembayaran.equals(getString(R.string.bayar_nanti), true)) {
                                getString(R.string.belum_dibayar)
                            } else {
                                getString(R.string.selesai)
                            }
                            database.child(id).child("status").setValue(statusBaru)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
