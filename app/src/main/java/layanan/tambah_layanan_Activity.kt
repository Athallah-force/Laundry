package layanan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_layanan
import com.google.firebase.database.FirebaseDatabase

class tambah_layanan_Activity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("layanan")
    lateinit var et_nama_layanan: EditText
    lateinit var et_harga: EditText
    lateinit var et_nama_cabang: EditText
    lateinit var bt_simpan_layanan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_layanan)
        init()
        bt_simpan_layanan.setOnClickListener {
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        et_nama_layanan = findViewById(R.id.et_nama_layanan)
        et_harga = findViewById(R.id.et_harga)
        et_nama_cabang = findViewById(R.id.et_cabang)
        bt_simpan_layanan = findViewById(R.id.bt_simpan_layanan)
    }

    fun cekValidasi() {
        val nama_layanan = et_nama_layanan.text.toString()
        val harga = et_harga.text.toString()
        val nama_cabang = et_nama_cabang.text.toString()

        //validasi data
        if (nama_layanan.isEmpty()) {
            et_nama_layanan.error = this.getString(R.string.validasi_nama_layanan)
            Toast.makeText(
                this@tambah_layanan_Activity,
                this.getString(R.string.validasi_nama_layanan),
                Toast.LENGTH_SHORT
            ).show()
            et_nama_layanan.requestFocus()
            return
        }
        if (harga.isEmpty()) {
            et_harga.error = this.getString(R.string.validasi_harga_layanan)
            Toast.makeText(
                this@tambah_layanan_Activity,
                this.getString(R.string.validasi_harga_layanan),
                Toast.LENGTH_SHORT
            ).show()
            et_harga.requestFocus()
            return
        }
        if (nama_cabang.isEmpty()) {
            et_nama_cabang.error = this.getString(R.string.validasi_nama_cabang)
            Toast.makeText(
                this@tambah_layanan_Activity,
                this.getString(R.string.validasi_nama_cabang),
                Toast.LENGTH_SHORT
            ).show()
            et_nama_cabang.requestFocus()
            return
        }

        simpan()


    }
    fun simpan() {
        val layananBaru = myRef.push()
        val layananId = layananBaru.key ?: return
        val data = model_layanan(
            idLayanan = layananId,
            namaLayanan = et_nama_layanan.text.toString(),
            harga = et_harga.text.toString(),
            namaCabang = et_nama_cabang.text.toString()
        )
        layananBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.tambah_layanan_simpan), Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("layanan_id", layananId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.tambah_layanan_gagal), Toast.LENGTH_SHORT).show()
            }


    }


}