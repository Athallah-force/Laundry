package com.athalah.laundry.cabang

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.adapter.adapter_data_cabang
import com.athalah.laundry.model_data.model_cabang
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Data_cabang : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("cabang")
    lateinit var rv_data_cabang: RecyclerView
    lateinit var tambah_cabang: FloatingActionButton
    lateinit var cabangList: ArrayList<model_cabang>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_cabang.layoutManager = layoutManager
        rv_data_cabang.setHasFixedSize(true)
        cabangList = arrayListOf<model_cabang>()

        tambah_cabang.setOnClickListener {
            val intent = Intent(this, Tambah_cabang::class.java)
            startActivity(intent)
        }
        getData()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rv_data_cabang = findViewById(R.id.rv_data_cabang)
        tambah_cabang = findViewById(R.id.bt_data_cabang_tambah)

    }
    fun getData(){
        val query = myRef.orderByChild("idCabang").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    cabangList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(model_cabang::class.java)
                        pegawai?.let { cabangList.add(it) }
                    }
                    val adapter = adapter_data_cabang(cabangList)
                    rv_data_cabang.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}