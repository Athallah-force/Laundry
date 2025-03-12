package pegawai

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
import com.athalah.laundry.adapter.adapter_data_pegawai
import com.athalah.laundry.adapter.adapter_data_pelanggan
import com.athalah.laundry.model_data.model_pegawai
import com.athalah.laundry.model_data.model_pelanggan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pegawai.tambah_pegawai_Activity

class data_pegawai_Activity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var rv_data_pegawai: RecyclerView
    lateinit var tambah_pegawai: FloatingActionButton
    lateinit var pegawaiList: ArrayList<model_pegawai>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)
        init()
         val layoutManager = LinearLayoutManager(this)
         layoutManager.reverseLayout = true
         layoutManager.stackFromEnd = true
         rv_data_pegawai.layoutManager = layoutManager
         rv_data_pegawai.setHasFixedSize(true)
         pegawaiList = arrayListOf<model_pegawai>()

         tambah_pegawai.setOnClickListener {
            val intent = Intent(this, tambah_pegawai_Activity::class.java)
            startActivity(intent)
        }
        getData()

        val fabTambahPegawai: FloatingActionButton = findViewById(R.id.bt_data_pegawai_tambah)
        fabTambahPegawai.setOnClickListener {
            val intent = Intent(this, tambah_pegawai_Activity::class.java)
            intent.putExtra("judul", (this.getString(R.string.tambah_pegawai)))
            intent.putExtra("id", "")
            intent.putExtra("nama", "")
            intent.putExtra("alamat", "")
            intent.putExtra("cabang", "")
            intent.putExtra("terdaftar", "")
            intent.putExtra("nohp", "")
            startActivity(intent)
        }












        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init(){
        rv_data_pegawai = findViewById(R.id.rv_data_pegawai)
        tambah_pegawai = findViewById(R.id.bt_data_pegawai_tambah)
    }
    fun getData() {
        val query = myRef.orderByChild("idPegawai").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    pegawaiList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(model_pegawai::class.java)
                        pegawai?.let{ pegawaiList.add(it) }
                    }
                    val adapter = adapter_data_pegawai(pegawaiList)
                    rv_data_pegawai.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

}
