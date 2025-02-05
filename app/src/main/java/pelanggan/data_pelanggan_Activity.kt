package pelanggan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.athalah.laundry.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class data_pelanggan_Activity : AppCompatActivity() {
    lateinit var cv_data_pelanggan: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_pelanggan)

        cv_data_pelanggan = findViewById(R.id.fab_tambah_pelanggan) // Gunakan ID yang benar

        cv_data_pelanggan.setOnClickListener {
            val intent = Intent(this, tambah_pelanggan_Activity::class.java)
            startActivity(intent)
        }
    }
}
