package pelanggan

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
import com.athalah.laundry.model_data.model_pelanggan
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class tambah_pelanggan_Activity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pelanggan")

    lateinit var tv_judul: TextView
    lateinit var et_namalengkap: EditText
    lateinit var et_alamat: EditText
    lateinit var et_nohp: EditText
    lateinit var bt_simpan: Button

    var idPelanggan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pelanggan)
        init()
        getData()

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
        tv_judul = findViewById(R.id.tv_tambah_pelanggan)
        et_namalengkap = findViewById(R.id.et_namalengkap)
        et_alamat = findViewById(R.id.et_alamat)
        et_nohp = findViewById(R.id.et_nohp)
        bt_simpan = findViewById(R.id.bt_simpan)
    }

    fun getData() {
        idPelanggan = intent.getStringExtra("id").toString()
        val judul = intent.getStringExtra("judul") ?: "Tambah"
        val nama = intent.getStringExtra("nama").toString()
        val alamat = intent.getStringExtra("alamat").toString()
        val nohp = intent.getStringExtra("nohp").toString()

        tv_judul.text = judul
        et_namalengkap.setText(nama)
        et_alamat.setText(alamat)
        et_nohp.setText(nohp)

        if (judul.equals("edit", ignoreCase = true)) {
            tv_judul.text = "Edit Pelanggan"
            mati()
            bt_simpan.text = "Edit"
        } else {
            tv_judul.text = "Tambah Pelanggan"
            hidup()
            et_namalengkap.requestFocus()
            bt_simpan.text = "Simpan"
        }

    }

    fun mati() {
        et_namalengkap.isEnabled = false
        et_alamat.isEnabled = false
        et_nohp.isEnabled = false
    }

    fun hidup() {
        et_namalengkap.isEnabled = true
        et_alamat.isEnabled = true
        et_nohp.isEnabled = true
    }

    fun update() {
        val pelangganRef = myRef.child(idPelanggan)
        val updateData = mapOf(
            "namaPelanggan" to et_namalengkap.text.toString(),
            "alamatPelanggan" to et_alamat.text.toString(),
            "noHPPelanggan" to et_nohp.text.toString()
        )

        pelangganRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Data gagal diupdate", Toast.LENGTH_SHORT).show()
        }
    }

    fun cekValidasi() {
        val nama = et_namalengkap.text.toString()
        val alamat = et_alamat.text.toString()
        val nohp = et_nohp.text.toString()

        if (nama.isEmpty()) {
            et_namalengkap.error = getString(R.string.validasi_nama_pelanggan)
            et_namalengkap.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            et_alamat.error = getString(R.string.validasi_alamat_pelanggan)
            et_alamat.requestFocus()
            return
        }
        if (nohp.isEmpty()) {
            et_nohp.error = getString(R.string.validasi_nohp_pelanggan)
            et_nohp.requestFocus()
            return
        }

        when (bt_simpan.text.toString()) {
            "Simpan" -> simpan()
            "Edit" -> {
                hidup()
                et_namalengkap.requestFocus()
                bt_simpan.text = "Perbarui"
            }
            "Perbarui" -> update()
        }
    }

    fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key ?: return
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val data = model_pelanggan(
            idPelanggan = pelangganId,
            namaPelanggan = et_namalengkap.text.toString(),
            alamatPelanggan = et_alamat.text.toString(),
            noHPPelanggan = et_nohp.text.toString(),
            terdaftar = currentTime
        )

        pelangganBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.tambah_pelanggan_sukses), Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("pelanggan_id", pelangganId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.tambah_pelanggan_gagal), Toast.LENGTH_SHORT).show()
            }
    }
}
