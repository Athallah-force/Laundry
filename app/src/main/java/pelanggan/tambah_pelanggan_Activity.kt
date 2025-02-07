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
    lateinit var tv_namalengkap: EditText
    lateinit var tv_alamat: EditText
    lateinit var tv_nohp: EditText
    lateinit var tv_cabang: EditText
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
        tv_tambah_pelanggan = findViewById(R.id.tv_tambah_pengguna)
        tv_namalengkap = findViewById(R.id.et_namalengkap)
        tv_alamat = findViewById(R.id.et_alamat)
        tv_nohp = findViewById(R.id.et_nohp)
        tv_cabang = findViewById(R.id.et_cabang)
        bt_simpan = findViewById(R.id.bt_simpan)
    }

    fun cekValidasi() {
        val nama = tv_namalengkap.text.toString()
        val alamat = tv_alamat.text.toString()
        val nohp = tv_nohp.text.toString()
        val cabang = tv_cabang.text.toString()
        //validasi data
        if (nama.isEmpty()) {
            tv_namalengkap.error = this.getString(R.string.validasi_nama_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_nama_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            tv_namalengkap.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            tv_alamat.error = this.getString(R.string.validasi_alamat_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_alamat_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            tv_alamat.requestFocus()
            return
        }
        if (nohp.isEmpty()) {
            tv_nohp.error = this.getString(R.string.validasi_nohp_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_nohp_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            tv_nohp.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            tv_cabang.error = this.getString(R.string.validasi_cabang_pelanggan)
            Toast.makeText(
                this@tambah_pelanggan_Activity,
                this.getString(R.string.validasi_cabang_pelanggan),
                Toast.LENGTH_SHORT
            ).show()
            tv_cabang.requestFocus()
            return
        }
        simpan()
        }
    fun simpan() {
        val pelangganBaru = myRef.push()
        val pelangganId = pelangganBaru.key
        val data = model_pelanggan(
            pelangganId.toString(),
            tv_namalengkap.text.toString(),
            tv_alamat.text.toString(),
            tv_nohp.text.toString(),
            tv_cabang.text.toString()
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