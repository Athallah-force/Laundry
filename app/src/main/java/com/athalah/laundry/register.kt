package com.athalah.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.athalah.laundry.model_data.model_user
import com.google.firebase.database.FirebaseDatabase

class register : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnDaftar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNama = findViewById(R.id.etnama_register)
        etNoHp = findViewById(R.id.etnomor_register)
        etPassword = findViewById(R.id.etpassword_register)
        btnDaftar = findViewById(R.id.btregister)

        btnDaftar.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nohp = etNoHp.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nama.isEmpty() || nohp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbRef = FirebaseDatabase.getInstance().getReference("users")

            // Cek apakah nomor HP sudah terdaftar
            dbRef.child(nohp).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    Toast.makeText(this, getString(R.string.hp), Toast.LENGTH_SHORT).show()
                } else {
                    val user = model_user(uid = nohp, nama = nama, nohp = nohp, password = password)

                    dbRef.child(nohp).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, getString(R.string.berhasil_regis), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Login_Activity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, getString(R.string.gagal_simpan, e.message), Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, getString(R.string.gagal_nomor, e.message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
