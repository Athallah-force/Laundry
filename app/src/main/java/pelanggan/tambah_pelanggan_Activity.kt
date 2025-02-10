package pelanggan

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
import com.athalah.laundry.model_data.model_pelanggan
import com.google.firebase.database.FirebaseDatabase


class tambah_pelanggan_Activity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")
    lateinit var tv_tambah_pelanggan: TextView
    lateinit var et_namalengkap: EditText
    lateinit var et_alamat: EditText
    lateinit var et_nohp: EditText
    lateinit var et_cabang: EditText
    lateinit var bt_simpan: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)
        init()
        bt_simpan.setOnClickListener {
            cekValidasi()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        tv_tambah_pelanggan = findViewById(R.id.tv_tambah_pelanggan)
        et_namalengkap = findViewById(R.id.et_namalengkap)
        et_alamat = findViewById(R.id.et_alamat)
        et_nohp = findViewById(R.id.et_nohp)
        et_cabang = findViewById(R.id.et_cabang)
        bt_simpan = findViewById(R.id.bt_simpan)
    }

    fun cekValidasi() {
        val nama = et_namalengkap.text.toString()
        val alamat = et_alamat.text.toString()
        val nohp = et_nohp.text.toString()
        val cabang = et_cabang.text.toString()
        //validasi data
        if (nama.isEmpty()) {
            et_namalengkap.error = this.getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_nama_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            et_namalengkap.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            et_alamat.error = this.getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_alamat_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            et_alamat.requestFocus()
            return
        }
        if (nohp.isEmpty()) {
            et_nohp.error = this.getString(R.string.validasi_nohp_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_nohp_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            et_nohp.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            et_cabang.error = this.getString(R.string.validasi_cabang_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_cabang_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            et_cabang.requestFocus()
            return
        }
        simpan()
        }
    fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key
        val data = model_pelanggan(
            pelangganId.toString(),
            et_namalengkap.text.toString(),
            et_alamat.text.toString(),
            et_nohp.text.toString(),
            et_cabang.text.toString()
        )
        pelangganBaru.setValue(data)
            .addOnCompleteListener {
                Toast.makeText(
                    this,
                    this.getString(R.string.tambah_pelanggan_sukses),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    this.getString(R.string.tambah_pelanggan_gagal),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
    }
}