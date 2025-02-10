package pelanggan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.adapter.adapter_data_pelanggan
import com.athalah.laundry.model_data.model_pelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class data_pelanggan_Activity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var rv_data_pelanggan: RecyclerView
    lateinit var tambah_pelanggan: FloatingActionButton
    lateinit var pelangganList: ArrayList<model_pelanggan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)
        init()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_pelanggan.layoutManager = layoutManager
        rv_data_pelanggan.setHasFixedSize(true)
        pelangganList = arrayListOf<model_pelanggan>()

        tambah_pelanggan.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan_Activity::class.java)
            startActivity(intent)
        }
        getdata()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init(){
        rv_data_pelanggan = findViewById(R.id.rv_data_pelanggan)
        tambah_pelanggan = findViewById(R.id.bt_data_pelanggan_tambah)
    }

    fun getdata(){
        val query = myRef.orderByChild("idpelanggan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    pelangganList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(model_pelanggan::class.java)
                        pelangganList.add(pegawai!!)
                    }


                    val adapter = adapter_data_pelanggan(pelangganList)
                    rv_data_pelanggan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@data_pelanggan_Activity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}
