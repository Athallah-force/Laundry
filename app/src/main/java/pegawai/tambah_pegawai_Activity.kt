package pegawai

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
import com.athalah.laundry.model_data.model_pegawai
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class tambah_pegawai_Activity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("pegawai")
    lateinit var tv_tambah_pegawai: TextView
    lateinit var tv_judul: TextView
    lateinit var et_namalengkap: EditText
    lateinit var et_alamat: EditText
    lateinit var et_nohp: EditText
    lateinit var et_cabang: EditText
    lateinit var bt_simpan: Button

    var idPegawai: String= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_pegawai)
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
        tv_judul = findViewById(R.id.tv_tambah_pegawai)
        tv_tambah_pegawai = findViewById(R.id.tv_tambah_pegawai)
        et_namalengkap = findViewById(R.id.et_namalengkap)
        et_alamat = findViewById(R.id.et_alamat)
        et_nohp = findViewById(R.id.et_nohp)

        et_cabang = findViewById(R.id.et_cabang)
        bt_simpan = findViewById(R.id.bt_simpan)
    }

    fun getData() {
        idPegawai = intent.getStringExtra("id").toString()
        val judul = intent.getStringExtra("judul").toString()
        val nama = intent.getStringExtra("nama").toString()
        val alamat = intent.getStringExtra("alamat").toString()
        val nohp = intent.getStringExtra("nohp").toString()
        val cabang = intent.getStringExtra("cabang").toString()
        tv_judul.text = judul
        et_namalengkap.setText(nama)
        et_alamat.setText(alamat)
        et_nohp.setText(nohp)
        et_cabang.setText(cabang)
        if(!tv_judul.text.equals("Tambah")){
            if(judul.equals("edit")){
                mati()
                bt_simpan.text = "Edit"
            }
        }else{
            hidup()
            et_namalengkap.requestFocus()
            bt_simpan.text = "Simpan"
        }
    }

    fun mati(){
        et_namalengkap.isEnabled = false
        et_alamat.isEnabled = false
        et_nohp.isEnabled = false
        et_cabang.isEnabled = false
    }

    fun hidup() {
        et_namalengkap.isEnabled = true
        et_alamat.isEnabled = true
        et_nohp.isEnabled = true
        et_cabang.isEnabled = true
    }

    fun update(){
        val pegawaiRef = database.getReference("pegawai").child(idPegawai)
        //buat map untuk update data
        val updateData = mutableMapOf<String, Any>()
        updateData["nama"] = et_namalengkap.text.toString()
        updateData["alamat"] = et_alamat.text.toString()
        updateData["nohp"] = et_nohp.text.toString()
        updateData["cabang"] = et_cabang.text.toString()
        pegawaiRef.updateChildren(updateData).addOnSuccessListener {
            Toast.makeText(this@tambah_pegawai_Activity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this@tambah_pegawai_Activity,"Data gagal diupdate",Toast.LENGTH_SHORT).show()
        }

    }




    fun cekValidasi() {
        val nama = et_namalengkap.text.toString()
        val alamat = et_alamat.text.toString()
        val nohp = et_nohp.text.toString()
        val cabang = et_cabang.text.toString()


        //validasi data
        if (nama.isEmpty()) {
            et_namalengkap.error = this.getString(R.string.validasi_nama_pegawai)
            Toast.makeText(
                this@tambah_pegawai_Activity,
                this.getString(R.string.validasi_nama_pegawai),
                Toast.LENGTH_SHORT
            ).show()
            et_namalengkap.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            et_alamat.error = this.getString(R.string.validasi_alamat_pegawai)
            Toast.makeText(
                this@tambah_pegawai_Activity,
                this.getString(R.string.validasi_alamat_pegawai),
                Toast.LENGTH_SHORT
            ).show()
            et_alamat.requestFocus()
            return
        }
        if (nohp.isEmpty()) {
            et_nohp.error = this.getString(R.string.validasi_nohp_pegawai)
            Toast.makeText(
                this@tambah_pegawai_Activity,
                this.getString(R.string.validasi_nohp_pegawai),
                Toast.LENGTH_SHORT
            ).show()
            et_nohp.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            et_cabang.error = this.getString(R.string.validasi_cabang_pegawai)
            Toast.makeText(
                this@tambah_pegawai_Activity,
                this.getString(R.string.validasi_cabang_pegawai),
                Toast.LENGTH_SHORT
            ).show()
            et_cabang.requestFocus()
            return
        }
        if (bt_simpan.text.equals("Simpan")) {
            simpan()
        }else if(bt_simpan.text.equals("Edit")){
            hidup()
            et_namalengkap.requestFocus()
            bt_simpan.text="Perbarui"
        }else if (bt_simpan.text.equals("Perbarui")) {
            update()
        }
    }

    fun simpan() {
        val pegawaiBaru = myRef.push() // Buat node baru di Firebase
        val pegawaiId = pegawaiBaru.key ?: return // Jika key null, langsung keluar
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val data = model_pegawai(
            id = pegawaiId,
            nama = et_namalengkap.text.toString(),
            alamat = et_alamat.text.toString(),
            nohp = et_nohp.text.toString(),
            terdaftar = currentTime, // Sekarang sesuai dengan model
            cabang = et_cabang.text.toString()
        )

        pegawaiBaru.setValue(data) // Simpan semua data sekaligus
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.tambah_pegawai_simpan), Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("pegawai_id", pegawaiId)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.tambah_pelanggan_gagal), Toast.LENGTH_SHORT).show()
            }
    }


}

