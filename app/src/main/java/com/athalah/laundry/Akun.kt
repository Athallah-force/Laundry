package com.athalah.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Akun : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etNoHp: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogout: Button
    private lateinit var btnAction: Button
    private lateinit var database: DatabaseReference

    private var isEditMode = false
    private lateinit var originalNoHp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_akun)

        etNama = findViewById(R.id.etnama_akun)
        etNoHp = findViewById(R.id.etnomor_akun)
        etPassword = findViewById(R.id.etpassword_akun)
        btnLogout = findViewById(R.id.btlogout)
        btnAction = findViewById(R.id.btAction)

        database = FirebaseDatabase.getInstance().getReference("users")

        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        originalNoHp = sharedPref.getString("no_hp", "") ?: ""

        if (originalNoHp.isEmpty()) {
            Toast.makeText(this, getString(R.string.tidak), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Ambil data dari Firebase
        database.child(originalNoHp).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataNama = snapshot.child("nama").value.toString()
                val dataNoHp = snapshot.child("nohp").value.toString()
                val dataPassword = snapshot.child("password").value.toString()

                etNama.setText(dataNama)
                etNoHp.setText(dataNoHp)
                etPassword.setText(dataPassword)
            } else {
                Toast.makeText(this, getString(R.string.data_tidak_ditemukan), Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.gagal_ambil_data), Toast.LENGTH_SHORT).show()
        }

        setEditMode(false)

        btnAction.setOnClickListener {
            if (!isEditMode) {
                setEditMode(true)
                isEditMode = true // Tambahkan ini!
                btnAction.text = getString(R.string.simpan)
            } else {
                val newNama = etNama.text.toString().trim()
                val newNoHp = etNoHp.text.toString().trim()
                val newPassword = etPassword.text.toString().trim()

                if (newNama.isEmpty() || newNoHp.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(this, getString(R.string.semua), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val newUser = mapOf(
                    "uid" to newNoHp,
                    "nama" to newNama,
                    "nohp" to newNoHp,
                    "password" to newPassword
                )

                if (newNoHp != originalNoHp) {
                    database.child(newNoHp).get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            Toast.makeText(this, getString(R.string.sudah_digunakan), Toast.LENGTH_SHORT).show()
                        } else {
                            database.child(originalNoHp).removeValue()
                            database.child(newNoHp).setValue(newUser).addOnSuccessListener {
                                updateLocalData(newNama, newNoHp, newPassword)
                                isEditMode = false // Tambahkan ini!
                            }
                        }
                    }
                } else {
                    database.child(originalNoHp).setValue(newUser).addOnSuccessListener {
                        updateLocalData(newNama, newNoHp, newPassword)
                        isEditMode = false // Tambahkan ini juga!
                    }
                }
            }
        }

        btnLogout.setOnClickListener {
            getSharedPreferences("USER_DATA", MODE_PRIVATE).edit().clear().apply()
            startActivity(Intent(this, Login_Activity::class.java))
            finish()
        }
    }

    private fun updateLocalData(nama: String, noHp: String, password: String) {
        val sharedPref = getSharedPreferences("USER_DATA", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("nama", nama)
            putString("no_hp", noHp)
            putString("password", password)
            apply()
        }

        originalNoHp = noHp // update key utama lokal
        Toast.makeText(this, getString(R.string.berhasil), Toast.LENGTH_SHORT).show()
        setEditMode(false)
        btnAction.text = "Edit"
        isEditMode = false
    }

    private fun setEditMode(editable: Boolean) {
        etNama.isEnabled = editable
        etNoHp.isEnabled = editable
        etPassword.isEnabled = editable
    }
}
