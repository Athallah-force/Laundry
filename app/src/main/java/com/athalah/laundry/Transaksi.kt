package com.athalah.laundry

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.adapter.adapter_tambahan_dipilih
import com.athalah.laundry.model_data.model_tambahkan
import com.athalah.laundry.model_data.model_transaksi
import com.google.firebase.FirebaseApp

class Transaksi : AppCompatActivity() {
    private lateinit var tv_nama_pelanggan: TextView
    private lateinit var tv_nohp_pelanggan: TextView
    private lateinit var tv_layanan: TextView
    private lateinit var tv_layanan_harga: TextView
    private lateinit var rv_Layanan_Tambahan: RecyclerView
    private lateinit var btn_pilih_pelanggan: Button
    private lateinit var btn_pilih_layanan: Button
    private lateinit var btn_tambahan: Button
    private lateinit var btn_transaksi_proses: Button


    private lateinit var adapterTambahan: adapter_tambahan_dipilih
    private val dataList = mutableListOf<model_transaksi>()

    private val pilihPelanggan = 1
    private val pilihLayanan= 2
    private val tambahTambahan = 3

    private var idPelanggan: String = ""
    private var idCabang: String = ""
    private var namaPelanggan: String = ""
    private var noHPPelanggan: String = ""
    private var idLayanan: String = ""
    private var namaLayanan: String = ""
    private var hargaLayanan: String = ""
    private var idTambahan: String = ""
    private var namaTambahan: String = ""
    private var hargaTambahan: String = ""
    private var idPegawai: String = ""

    private lateinit var sharedPref: SharedPreferences
    private val listTambahan = mutableListOf<model_tambahkan>()


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaksi)
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE)
        idCabang = sharedPref.getString("idCabang", "").toString()
        idPegawai = sharedPref.getString("idPegawai", "").toString()
        init()
        FirebaseApp.initializeApp(this)
        val layoutManager = LinearLayoutManager(this)
        rv_Layanan_Tambahan.layoutManager = layoutManager
        rv_Layanan_Tambahan.setHasFixedSize(true)

        adapterTambahan = adapter_tambahan_dipilih(listTambahan) { tambahan ->
            listTambahan.remove(tambahan)
            adapterTambahan.notifyDataSetChanged()
        }
        rv_Layanan_Tambahan.adapter = adapterTambahan

        btn_pilih_pelanggan.setOnClickListener{
            val intent = Intent(this, PilihPelanggan::class.java)
            startActivityForResult(intent, pilihPelanggan)
        }
        btn_pilih_layanan.setOnClickListener{
            val intent = Intent(this, PilihLayanan::class.java)
            startActivityForResult(intent, pilihLayanan)
        }
        btn_tambahan.setOnClickListener{
            val intent = Intent(this, pilih_tambahan::class.java)
            startActivityForResult(intent, tambahTambahan)
        }
        val btnProsesTransaksi: Button = findViewById(R.id.bt_transaksi_proses)
        btnProsesTransaksi.setOnClickListener {
            if (namaPelanggan.isEmpty() || namaLayanan.isEmpty()) {
                Toast.makeText(this, getString(R.string.lengkapi_data), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, konfirmasiActivity::class.java)
            intent.putExtra("namaPelanggan", namaPelanggan)
            intent.putExtra("noHp", noHPPelanggan)
            intent.putExtra("layananUtama", namaLayanan)
            val hargaUtamaFix = hargaLayanan.replace(".", "").toIntOrNull() ?: 0
            intent.putExtra("hargaUtama", hargaUtamaFix)


            // Kirim list tambahan sebagai ArrayList<String> ("nama|harga")
            val tambahanStrings = ArrayList<String>()
            for (tambahan in listTambahan) {
                tambahanStrings.add("${tambahan.idTambahan}|${tambahan.namaTambahan}|${tambahan.harga}")

            }
            intent.putStringArrayListExtra("layananTambahan", tambahanStrings)

            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.transaksi)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun init() {
        tv_nama_pelanggan = findViewById(R.id.tvNamaPelangganLabel)
        tv_nohp_pelanggan = findViewById(R.id.tvNoHP)
        tv_layanan = findViewById(R.id.tvNamaLayananLabel)
        tv_layanan_harga = findViewById(R.id.tvHargaLabel)
        rv_Layanan_Tambahan = findViewById(R.id.rv_LayananTambahan)
        btn_pilih_pelanggan = findViewById(R.id.btnPilihPelanggan)
        btn_pilih_layanan = findViewById(R.id.btnPilihLayanan)
        btn_tambahan = findViewById(R.id.bt_transaksi_tambah)



    }

    @Deprecated("this method has been deprecated in favor of using the activity result API")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pilihPelanggan && resultCode == RESULT_OK) {
            idPelanggan = data?.getStringExtra("id").toString()
            val nama = data?.getStringExtra("nama")
            val nohp = data?.getStringExtra("nohp")
            tv_nama_pelanggan.text = nama
            tv_nohp_pelanggan.text = nohp

            namaPelanggan = nama.toString()
            noHPPelanggan = nohp.toString()
        }
        if (requestCode == pilihLayanan && resultCode == RESULT_OK) {
            idLayanan = data?.getStringExtra("id").toString()
            namaLayanan = data?.getStringExtra("nama").toString()
            hargaLayanan = data?.getStringExtra("harga").toString()

            tv_layanan.text = namaLayanan
            tv_layanan_harga.text = hargaLayanan

            Toast.makeText(
                this,
                getString(R.string.dipilih, namaLayanan),
                Toast.LENGTH_SHORT
            ).show()

        }
        if (requestCode == tambahTambahan && resultCode == RESULT_OK) {
            val id = data?.getStringExtra("id") ?: return
            val nama = data.getStringExtra("nama") ?: return
            val harga = data.getStringExtra("harga") ?: return


            val tambahan = model_tambahkan(id, nama, harga)

            // Cek apakah sudah ada item dengan ID yang sama
            val sudahAda = listTambahan.any { it.idTambahan == id }
            if (!sudahAda) {
                listTambahan.add(tambahan)
                adapterTambahan.notifyDataSetChanged()
                rv_Layanan_Tambahan.visibility = View.VISIBLE


                Toast.makeText(this, getString(R.string.tambahkan, nama), Toast.LENGTH_SHORT).show()


            } else {
                Toast.makeText(this, getString(R.string.sudah_ada), Toast.LENGTH_SHORT).show()
            }


        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("namaPelanggan", namaPelanggan)
        outState.putString("noHPPelanggan", noHPPelanggan)
        outState.putString("namaLayanan", namaLayanan)
        outState.putString("hargaLayanan", hargaLayanan)

        // Simpan list tambahan sebagai ArrayList of string: "id|nama|harga"
        val tambahanStrings = ArrayList<String>()
        listTambahan.forEach {
            tambahanStrings.add("${it.idTambahan}|${it.namaTambahan}|${it.harga}")
        }
        outState.putStringArrayList("layananTambahan", tambahanStrings)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        namaPelanggan = savedInstanceState.getString("namaPelanggan", "")
        noHPPelanggan = savedInstanceState.getString("noHPPelanggan", "")
        namaLayanan = savedInstanceState.getString("namaLayanan", "")
        hargaLayanan = savedInstanceState.getString("hargaLayanan", "")

        tv_nama_pelanggan.text = namaPelanggan
        tv_nohp_pelanggan.text = noHPPelanggan
        tv_layanan.text = namaLayanan
        tv_layanan_harga.text = hargaLayanan

        val tambahanStrings = savedInstanceState.getStringArrayList("layananTambahan") ?: arrayListOf()
        listTambahan.clear()
        tambahanStrings.forEach {
            val parts = it.split("|")
            if (parts.size == 3) {
                val model = model_tambahkan(parts[0], parts[1], parts[2])
                listTambahan.add(model)
            }
        }

        if (listTambahan.isNotEmpty()) {
            rv_Layanan_Tambahan.visibility = View.VISIBLE
        }

        adapterTambahan.notifyDataSetChanged()
    }


}