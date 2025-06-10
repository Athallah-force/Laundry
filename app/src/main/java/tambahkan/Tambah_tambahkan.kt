package tambahkan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_tambahkan
import com.google.firebase.database.FirebaseDatabase

class Tambah_tambahkan : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("tambahkan")
    lateinit var et_nama_tambahkan: EditText
    lateinit var et_harga: EditText
    lateinit var bt_simpan_tambahkan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_tambahkan)
        init()
        bt_simpan_tambahkan.setOnClickListener {
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        et_nama_tambahkan = findViewById(R.id.et_nama_tambahkan)
        et_harga = findViewById(R.id.et_harga)
        bt_simpan_tambahkan = findViewById(R.id.bt_simpan_tambahkan)

    }

    fun cekValidasi() {
        val nama_tambahkan = et_nama_tambahkan.text.toString()
        val harga = et_harga.text.toString()

        //validasi data
        if (nama_tambahkan.isEmpty()) {
            et_nama_tambahkan.error = this.getString(R.string.validasi_nama_tambahkan)
            Toast.makeText(
                this@Tambah_tambahkan,
                this.getString(R.string.validasi_nama_tambahkan),
                Toast.LENGTH_SHORT
            ).show()
            et_nama_tambahkan.requestFocus()
            return
        }
        if (harga.isEmpty()) {
            et_harga.error = this.getString(R.string.validasi_harga_tambahkan)
            Toast.makeText(
                this@Tambah_tambahkan,
                this.getString(R.string.validasi_harga_tambahkan),
                Toast.LENGTH_SHORT
            ).show()
            et_harga.requestFocus()
            return
        }
        simpan()

    }
    fun simpan(){
        val tambahanBaru = myRef.push()
        val tambahanId = tambahanBaru.key ?: return
        val data = model_tambahkan(
            idTambahan = tambahanId,
            namaTambahan = et_nama_tambahkan.text.toString(),
            harga = et_harga.text.toString()
        )
        tambahanBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Simpan Data", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("tambahan_id", tambahanId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Gagal Simpan Data", Toast.LENGTH_SHORT).show()
            }


    }
}