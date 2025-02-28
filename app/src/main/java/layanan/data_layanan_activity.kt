package layanan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.adapter.adapter_data_layanan
import com.athalah.laundry.model_data.model_layanan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class data_layanan_activity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var rv_data_layanan: RecyclerView
    lateinit var tambah_layanan: FloatingActionButton
    lateinit var layananList: ArrayList<model_layanan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_layanan)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_layanan.layoutManager = layoutManager
        rv_data_layanan.setHasFixedSize(true)
        layananList = arrayListOf<model_layanan>()

        tambah_layanan.setOnClickListener {
            val intent = Intent(this, tambah_layanan_Activity::class.java)
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
        rv_data_layanan = findViewById(R.id.rv_data_layanan)
        tambah_layanan = findViewById(R.id.bt_data_layanan_tambah)

    }
    fun getData() {
        val query = myRef.orderByChild("idLayanan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    layananList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val pegawai = dataSnapshot.getValue(model_layanan::class.java)
                        pegawai?.let{ layananList.add(it) }
                    }
                    val adapter = adapter_data_layanan(layananList)
                    rv_data_layanan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

}