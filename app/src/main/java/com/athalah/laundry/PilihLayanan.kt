package com.athalah.laundry

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.adapter.adapter_pilih_layanan
import com.athalah.laundry.adapter.adapter_pilih_pelanggan
import com.athalah.laundry.model_data.model_layanan
import com.athalah.laundry.model_data.model_pelanggan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PilihLayanan : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layananList: ArrayList<model_layanan>
    private lateinit var adapter: adapter_pilih_layanan
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tvKosong: TextView
    private val TAG = "PilihLayanan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_layanan)

        // Initialize views
        recyclerView = findViewById(R.id.rv_data_layanan)
        tvKosong = findViewById(R.id.tvKosong)

        // Initialize RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize ArrayList
        layananList = ArrayList()

        // Initialize adapter and set to RecyclerView
        adapter = adapter_pilih_layanan(layananList)
        recyclerView.adapter = adapter

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("layanan")

        // Set initial visibility (show loading)
        tvKosong.text = "Loading..."
        tvKosong.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        // Load data form Firebase
        loadLayananData()

    }

    private fun loadLayananData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: Data received from Firebase")
                layananList.clear()

                if (snapshot.exists()) {
                    Log.d(TAG, "Snapshot exists with ${snapshot.childrenCount} items")

                    for (layananSnapshot in snapshot.children) {
                        try {
                            val layanan = layananSnapshot.getValue(model_layanan::class.java)
                            Log.d(TAG, "Data services: $layanan")

                            if (layanan != null) {
                                val updatedLayanan = model_layanan(
                                    layananSnapshot.key ?: "",
                                    layanan.namaLayanan,
                                    layanan.harga
                                )
                                layananList.add(updatedLayanan)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing data", e)

                        }
                    }

                    Log.d(TAG, "Final list size: ${layananList.size}")

                    // Update adapter with new data
                    adapter.notifyDataSetChanged()

                    // Show RecyclerView and hide empty text if we have data
                    if (layananList.isNotEmpty()) {
                        Log.d(TAG, "Showing RecyclerView")
                        recyclerView.visibility = View.VISIBLE
                        tvKosong.visibility = View.GONE
                    } else {
                        Log.d(TAG, "List is empty, showing empty text")
                        recyclerView.visibility = View.GONE
                        tvKosong.visibility = View.VISIBLE
                        tvKosong.text = "Data not fund"
                    }
                } else {
                    // No data exists
                    Log.d(TAG, "Snapshot does not exist")
                    recyclerView.visibility = View.GONE
                    tvKosong.visibility = View.VISIBLE
                    tvKosong.text = "Data not fund"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e(TAG, "Database error: ${error.message}")
                recyclerView.visibility = View.GONE
                tvKosong.visibility = View.VISIBLE
                tvKosong.text = "Error: ${error.message}"

            }
        })
    }
}