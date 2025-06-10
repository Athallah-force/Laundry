package com.athalah.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.athalah.laundry.model_data.model_user

class Login_Activity : AppCompatActivity() {

    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etNoHp = findViewById(R.id.etnomor_login)
        etPassword = findViewById(R.id.etpassword_login)
        btnLogin = findViewById(R.id.btlogin)
        tvRegister = findViewById(R.id.buat_register)

        dbRef = FirebaseDatabase.getInstance().getReference("users")

        // Pindah ke halaman register
        tvRegister.setOnClickListener {
            startActivity(Intent(this, register::class.java))
        }

        // Proses login
        btnLogin.setOnClickListener {
            val inputNoHp = etNoHp.text.toString().trim()
            val inputPassword = etPassword.text.toString().trim()

            if (inputNoHp.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, getString(R.string.sudah_diisi), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cari data user berdasarkan no HP
            dbRef.child(inputNoHp).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val user = snapshot.getValue(model_user::class.java)
                    if (user != null && user.password == inputPassword) {
                        // Simpan data login ke SharedPreferences
                        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                        sharedPref.edit().apply {
                            putString("uid", inputNoHp) // Karena key-nya no HP
                            putString("nama", user.nama)
                            putString("no_hp", user.nohp)
                            putString("password", user.password)
                            apply()
                        }

                        // Login sukses, lanjut ke MainActivity
                        Toast.makeText(this, getString(R.string.login), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, getString(R.string.password), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.tidak), Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, getString(R.string.gagal_login, it.message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
