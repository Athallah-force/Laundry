package tambahkan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.adapter.adapter_data_tambahan
import com.athalah.laundry.model_data.model_tambahkan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Data_tambahkan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahkan")
    lateinit var rv_data_tambahkan: RecyclerView
    lateinit var Tambah_tambahkan: FloatingActionButton
    lateinit var TambahkanList: ArrayList<model_tambahkan>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahkan)
        init()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rv_data_tambahkan.layoutManager = layoutManager
        rv_data_tambahkan.setHasFixedSize(true)
        TambahkanList = arrayListOf<model_tambahkan>()

        Tambah_tambahkan.setOnClickListener {
            val intent = Intent(this, tambahkan.Tambah_tambahkan::class.java)
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
        rv_data_tambahkan = findViewById(R.id.rv_data_tambahkan)
        Tambah_tambahkan = findViewById(R.id.bt_data_tambahkan_tambah)
    }
    fun getData(){
        val query = myRef.orderByChild("idTambahkan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshoot: DataSnapshot) {
                if (snapshoot.exists()){
                    TambahkanList.clear()
                    for (layananSnapshot in snapshoot.children){
                        val layanan = layananSnapshot.getValue(model_tambahkan::class.java)
                        TambahkanList.add(layanan!!)
                    }
                    val adapter = adapter_data_tambahan(TambahkanList)
                    rv_data_tambahkan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })

    }
}