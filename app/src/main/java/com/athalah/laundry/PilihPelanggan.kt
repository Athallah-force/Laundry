package com.athalah.laundry

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.adapter.adapter_pilih_pelanggan
import com.athalah.laundry.model_data.model_pelanggan
import com.google.firebase.database.*

class PilihPelanggan : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pelangganList: ArrayList<model_pelanggan>
    private lateinit var adapter: adapter_pilih_pelanggan
    private lateinit var databaseReference: DatabaseReference
    private lateinit var tvKosong: TextView
    private val TAG = "PilihPelanggan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_pelanggan)

        // Initialize views
        recyclerView = findViewById(R.id.rv_data_pelanggan)
        tvKosong = findViewById(R.id.tvKosong)

        // Initialize RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize ArrayList
        pelangganList = ArrayList()

        // Initialize adapter and set to RecyclerView
        adapter = adapter_pilih_pelanggan(pelangganList)
        recyclerView.adapter = adapter

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("pelanggan")

        // Set initial visibility (show loading)
        tvKosong.text = "Loading..."
        tvKosong.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        // Load data from Firebase
        loadPelangganData()
    }

    private fun loadPelangganData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: Data received from Firebase")
                pelangganList.clear()

                if (snapshot.exists()) {
                    Log.d(TAG, "Snapshot exists with ${snapshot.childrenCount} items")

                    for (pelangganSnapshot in snapshot.children) {
                        try {
                            val pelanggan = pelangganSnapshot.getValue(model_pelanggan::class.java)
                            Log.d(TAG, "Data customers: $pelanggan")

                            if (pelanggan != null) {
                                val updatedPelanggan = model_pelanggan(
                                    pelangganSnapshot.key ?: "",
                                    pelanggan.namaPelanggan,
                                    pelanggan.alamatPelanggan,
                                    pelanggan.noHPPelanggan
                                )
                                pelangganList.add(updatedPelanggan)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing data", e)
                        }
                    }

                    Log.d(TAG, "Final list size: ${pelangganList.size}")

                    // Update adapter with new data
                    adapter.notifyDataSetChanged()

                    // Show RecyclerView and hide empty text if we have data
                    if (pelangganList.isNotEmpty()) {
                        Log.d(TAG, "Showing RecyclerView")
                        recyclerView.visibility = View.VISIBLE
                        tvKosong.visibility = View.GONE
                    } else {
                        Log.d(TAG, "List is empty, showing empty text")
                        recyclerView.visibility = View.GONE
                        tvKosong.visibility = View.VISIBLE
                        tvKosong.text = "Data not found"
                    }
                } else {
                    // No data exists
                    Log.d(TAG, "Snapshot does not exist")
                    recyclerView.visibility = View.GONE
                    tvKosong.visibility = View.VISIBLE
                    tvKosong.text = "Data not found"
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