package pegawai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.athalah.laundry.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class data_pegawai_Activity : AppCompatActivity() {
    lateinit var cv_data_pegawai: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pegawai)

        cv_data_pegawai = findViewById(R.id.bt_data_pengguna_tambah) // Gunakan ID yang benar

        cv_data_pegawai.setOnClickListener {
            val intent = Intent(this, tambah_pegawai_Activity::class.java)
            startActivity(intent)
        }
    }
}
