package com.athalah.laundry

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.model_data.model_tambahkan
import com.athalah.laundry.adapter.adapter_konfirmasi_tambahan
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.Locale

class konfirmasiActivity : AppCompatActivity() {

    private var hargaUtama: Int = 0
    private var total: Int = 0

    fun formatRupiah(number: Int): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(number)
    }

    private fun showMetodePembayaranDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_metode_pembayaran, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        fun pindahKeStruk(metode: String) {
            val nama = textNama.text.toString()
            val hp = textHp.text.toString()
            val layanan = textLayanan.text.toString()
            val tambahanList = intent.getStringArrayListExtra("layananTambahan")

            // Konversi ke model_tambahkan
            val tambahanModelList = tambahanList?.mapNotNull {
                val parts = it.split("|")
                if (parts.size == 3) {
                    model_tambahkan(parts[0], parts[1], parts[2])
                } else null
            } ?: arrayListOf()

            // 🔥 Firebase Simpan Transaksi
            // Firebase Simpan Transaksi
            val database = FirebaseDatabase.getInstance().getReference("transaksi")
            val transaksiId = database.push().key!!

            val statusPembayaran = if (metode == "Bayar Nanti") "Belum Dibayar" else "Lunas"
            val statusPengambilan = "Belum Diambil"

            val transaksiData = mapOf(
                "id" to transaksiId,
                "nama_pelanggan" to nama,
                "noHp" to hp,
                "layanan_utama" to layanan,
                "harga_utama" to hargaUtama,
                "total_bayar" to total,
                "metode_pembayaran" to metode,
                "status_pembayaran" to statusPembayaran,
                "status_pengambilan" to statusPengambilan,
                "timestamp" to System.currentTimeMillis()
            )

            database.child(transaksiId).setValue(transaksiData)



            database.child(transaksiId).setValue(transaksiData)
                .addOnSuccessListener {
                    val intent = Intent(this@konfirmasiActivity, struk_pembayaran::class.java).apply {
                        putExtra("metode_pembayaran", metode)
                        putExtra("nama_pelanggan", nama)
                        putExtra("noHp", hp)
                        putExtra("nama_layanan", layanan)
                        putExtra("harga_layanan", hargaUtama.toDouble())
                        putExtra("total_bayar", total.toDouble())
                        putExtra("layanan_tambahan", ArrayList(tambahanModelList))
                    }

                    val statusPembayaran: String
                    val statusPengambilan: String

                    if (metode == "Bayar Nanti") {
                        statusPembayaran = "Belum Dibayar"
                        statusPengambilan = "Belum Diambil"
                    } else {
                        statusPembayaran = "Belum Diambil" // Karena sudah dibayar, tapi belum diambil
                        statusPengambilan = "Lunas"
                    }

                    intent.putExtra("status_pembayaran", statusPembayaran)
                    intent.putExtra("status_pengambilan", statusPengambilan)


                    startActivity(intent)
                    dialog.dismiss()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@konfirmasiActivity, getString(R.string.gagal_transaksi), Toast.LENGTH_SHORT).show()
                }
        }

        dialogView.findViewById<Button>(R.id.buttonBayarNanti).setOnClickListener {
            pindahKeStruk("Bayar Nanti")
        }

        dialogView.findViewById<Button>(R.id.buttonTunai).setOnClickListener {
            pindahKeStruk("Tunai")
        }

        dialogView.findViewById<Button>(R.id.buttonQRIS).setOnClickListener {
            pindahKeStruk("QRIS")
        }

        dialogView.findViewById<Button>(R.id.buttonDANA).setOnClickListener {
            pindahKeStruk("DANA")
        }

        dialogView.findViewById<Button>(R.id.buttonGoPay).setOnClickListener {
            pindahKeStruk("GoPay")
        }

        dialogView.findViewById<Button>(R.id.buttonOVO).setOnClickListener {
            pindahKeStruk("OVO")
        }

        dialogView.findViewById<TextView>(R.id.buttonBatal).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private lateinit var textNama: TextView
    private lateinit var textHp: TextView
    private lateinit var textLayanan: TextView
    private lateinit var textHargaUtama: TextView
    private lateinit var textTotalBayar: TextView
    private lateinit var recyclerTambahan: RecyclerView
    private lateinit var btnBayar: Button
    private lateinit var btnBatal: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi)

        // Bind Views
        textNama = findViewById(R.id.textNamaPelanggan)
        textHp = findViewById(R.id.textNoHp)
        textLayanan = findViewById(R.id.textLayananUtama)
        textHargaUtama = findViewById(R.id.textHargaUtama)
        textTotalBayar = findViewById(R.id.textTotalBayar)
        recyclerTambahan = findViewById(R.id.recyclerLayananTambahan)
        btnBayar = findViewById(R.id.buttonBayar)
        btnBatal = findViewById(R.id.buttonBatal)

        // Ambil data dari intent
        val nama = intent.getStringExtra("namaPelanggan")
        val hp = intent.getStringExtra("noHp")
        val layanan = intent.getStringExtra("layananUtama")
        hargaUtama = intent.getIntExtra("hargaUtama", 0)
        val tambahanListString = intent.getStringArrayListExtra("layananTambahan")

        // Tampilkan data utama
        textNama.text = nama ?: "nama tidak tersedia"
        textHp.text = hp ?: "-"
        textLayanan.text = layanan ?: "layanan tidak tersedia"
        textHargaUtama.text = formatRupiah(hargaUtama)
        textTotalBayar.text = formatRupiah(hargaUtama)

        // Konversi layanan tambahan
        val tambahanList = tambahanListString?.mapNotNull {
            val parts = it.split("|")
            if (parts.size == 3) {
                model_tambahkan(parts[0], parts[1], parts[2])
            } else null
        }

        // Hitung total
        total = hargaUtama
        for (tambahan in tambahanList!!) {
            val harga = tambahan.harga?.replace(".", "")?.toIntOrNull() ?: 0
            total += harga
        }

        textTotalBayar.text = formatRupiah(total)

        // Setup RecyclerView
        recyclerTambahan.layoutManager = LinearLayoutManager(this)
        recyclerTambahan.adapter = adapter_konfirmasi_tambahan(ArrayList(tambahanList))

        // Tombol aksi
        btnBayar.setOnClickListener {
            showMetodePembayaranDialog()
        }

        btnBatal.setOnClickListener {
            finish()
        }
    }
}
