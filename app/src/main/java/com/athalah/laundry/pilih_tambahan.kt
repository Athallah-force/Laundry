package com.athalah.laundry

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
import com.athalah.laundry.adapter.adapter_data_tambahan
import com.athalah.laundry.adapter.adapter_pilih_tambahan
import com.athalah.laundry.model_data.model_tambahkan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class pilih_tambahan : AppCompatActivity() {
        private lateinit var recyclerView: RecyclerView
        private lateinit var tambahanList: ArrayList<model_tambahkan>
        private lateinit var adapter: adapter_pilih_tambahan
        private lateinit var databaseReference: DatabaseReference
        private lateinit var tvKosong: TextView
        private val TAG = "pilih_tambahan"

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_pilih_tambahan)

            // Initialize views
            recyclerView = findViewById(R.id.rv_data_tambahkan)
            tvKosong = findViewById(R.id.tvKosong)

            // Initialize RecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this)

            // Initialize ArrayList
            tambahanList = ArrayList()

            adapter = adapter_pilih_tambahan(tambahanList)
            recyclerView.adapter = adapter


            // Initialize Firebase database reference
            databaseReference = FirebaseDatabase.getInstance().getReference("tambahkan")

            // Set initial visibility (show loading)
            tvKosong.text = "Loading..."
            tvKosong.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

            //Load data form Firebase
            loadTambahanData()

        }

        private fun loadTambahanData() {
            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange: Data received from Firebase")
                    tambahanList.clear()

                    if (snapshot.exists()) {
                        Log.d(TAG, "Snapshot exists with ${snapshot.childrenCount} items")

                        for (tambahanSnapshot in snapshot.children) {
                            try {
                                val tambahan = tambahanSnapshot.getValue(model_tambahkan::class.java)
                                Log.d(TAG, "Additional data: $tambahan")
                                Log.d("FirebaseDebug", "Data dari Firebase: $tambahan")

                                if (tambahan != null) {
                                    val updatedTambahan = model_tambahkan(
                                        tambahanSnapshot.key ?: "",
                                        tambahan.namaTambahan,
                                        tambahan.harga
                                    )
                                    tambahanList.add(updatedTambahan)
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error parsing data", e)

                            }

                        }

                        Log.d(TAG, "Final list size: ${tambahanList.size}")


                        // Show RecyclerView and hide empty text if we have data
                        if (tambahanList.isNotEmpty()) {
                            Log.d(TAG, "Showing RecyclerView")
                            val adapter = adapter_pilih_tambahan(tambahanList)
                            recyclerView.adapter = adapter

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