package com.athalah.laundry

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.adapter.adapter_tambahan_dipilih
import com.athalah.laundry.model_data.model_laporan
import com.athalah.laundry.model_data.model_tambahkan
import java.io.OutputStream
import java.util.Date
import java.util.Locale
import java.util.UUID

class struk_pembayaran : AppCompatActivity() {

    private lateinit var tvTanggal: TextView
    private lateinit var tvIdTransaksi: TextView
    private lateinit var tvNamaPelanggan: TextView
    private lateinit var tvLayananUtama: TextView
    private lateinit var tvHargaLayanan: TextView
    private lateinit var rvTambahan: RecyclerView
    private lateinit var tvSubtotalTambahan: TextView
    private lateinit var tvTotalBayar: TextView
    private lateinit var btnCetak: Button
    private lateinit var btnKirimWhatsapp: Button
    private lateinit var btnHome: Button

    private val listTambahan = ArrayList<model_tambahkan>()
    private lateinit var adapter: adapter_tambahan_dipilih

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    private val printerMAC = "DC:0D:51:A7:FF:7A"
    // Ganti dengan alamat MAC printermu
    private val printerUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk_pembayaran)

        initViews()
        requestBluetoothPermissionIfNeeded()
        setupRecyclerView()
        loadDataFromIntent()
        setupPrintButton()
        setupWhatsappButton()
        setupKembaliButton()

    }


    private fun initViews() {
        tvTanggal = findViewById(R.id.tvTanggal)
        tvIdTransaksi = findViewById(R.id.tvIdTransaksi)
        tvNamaPelanggan = findViewById(R.id.tvNamaPelanggan)
        tvLayananUtama = findViewById(R.id.tvLayananUtama)
        tvHargaLayanan = findViewById(R.id.tvHargaLayanan)
        rvTambahan = findViewById(R.id.rvRincianTambahan)
        tvSubtotalTambahan = findViewById(R.id.tvSubtotalTambahan)
        tvTotalBayar = findViewById(R.id.tvTotalBayar)
        btnCetak = findViewById(R.id.btnCetak)
        btnKirimWhatsapp = findViewById(R.id.btnKirimWhatsapp)
        btnHome = findViewById(R.id.btnhome)

    }
    private fun requestBluetoothPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.BLUETOOTH_CONNECT), 100)
        }
    }

    private fun setupRecyclerView() {
        adapter = adapter_tambahan_dipilih(listTambahan) {}
        rvTambahan.layoutManager = LinearLayoutManager(this)
        rvTambahan.adapter = adapter
    }

    private fun loadDataFromIntent() {
        val namaPelanggan = intent.getStringExtra("nama_pelanggan") ?: "-"
        val namaLayanan = intent.getStringExtra("nama_layanan") ?: "-"
        val hargaLayanan = intent.getDoubleExtra("harga_layanan", 0.0)
        val totalBayar = intent.getDoubleExtra("total_bayar", 0.0)
        val idTransaksi = intent.getStringExtra("id_transaksi") ?: UUID.randomUUID().toString()
        @Suppress("UNCHECKED_CAST")
        val tambahan = intent.getSerializableExtra("layanan_tambahan") as? ArrayList<model_tambahkan> ?: arrayListOf()


        val tanggal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        tvTanggal.text = tanggal
        tvIdTransaksi.text = idTransaksi
        tvNamaPelanggan.text = namaPelanggan
        tvLayananUtama.text = namaLayanan
        tvHargaLayanan.text = formatRupiah(hargaLayanan)

        listTambahan.clear()
        listTambahan.addAll(tambahan)
        adapter.notifyDataSetChanged()

        val subtotal = tambahan.sumOf { extractHargaFromString(it.harga ?: "0") }
        tvSubtotalTambahan.text = formatRupiah(subtotal)
        tvTotalBayar.text = formatRupiah(totalBayar)

        simpanKeFirebase()
    }

    private fun simpanKeFirebase() {
        val database = FirebaseDatabase.getInstance().reference
        val laporanRef = database.child("laporan")

        val idTransaksi = tvIdTransaksi.text.toString()
        val nama = tvNamaPelanggan.text.toString()
        val tanggal = tvTanggal.text.toString()
        val layanan = tvLayananUtama.text.toString()
        val harga = tvHargaLayanan.text.toString()
        val metode = intent.getStringExtra("metode_pembayaran") ?: "-"
        val totalBayar = tvTotalBayar.text.toString()
        val statusPembayaran: String
        val statusPengambilan: String

        if (metode == "Bayar Nanti") {
            statusPembayaran = "Belum Dibayar"
            statusPengambilan = "Belum Diambil"
        } else {
            statusPembayaran = "Belum Diambil"
            statusPengambilan = "Belum Diambil"
        }


        val tambahan = listTambahan.map {
            "${it.namaTambahan} - ${it.harga}"
        }

        val laporan = model_laporan(
            idTransaksi = idTransaksi,
            namaPelanggan = nama,
            tanggal = tanggal,
            layananUtama = layanan,
            tambahan = tambahan,
            totalBayar = totalBayar,
            status = statusPembayaran,
            metodePembayaran = metode,
            status_pembayaran = statusPembayaran,
            status_pengambilan = statusPengambilan
        )


        laporanRef.child(idTransaksi).setValue(laporan)
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.tersimpan), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.gagal), Toast.LENGTH_SHORT).show()
            }
    }

    private fun extractHargaFromString(harga: String): Double {
        val cleaned = harga.replace("Rp", "").replace(".", "").replace(",", ".").trim()
        return cleaned.toDoubleOrNull() ?: 0.0
    }

    private fun formatRupiah(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return formatter.format(amount)
    }

    private fun setupPrintButton() {
        btnCetak.setOnClickListener {
            val message = StringBuilder().apply {
                append("\n========= INVOICE =========\n")
                append("ID       : ${tvIdTransaksi.text}\n")
                append("Tanggal  : ${tvTanggal.text}\n")
                append("Pelanggan: ${tvNamaPelanggan.text}\n")
                append("-----------------------------\n")
                append("${tvLayananUtama.text.toString().padEnd(10)} : ${tvHargaLayanan.text}\n")

                if (listTambahan.isNotEmpty()) {
                    append("\nRincian Tambahan:\n")
                    listTambahan.forEachIndexed { i, item ->
                        append("${i + 1}. ${item.namaTambahan?.padEnd(10)} - Rp${item.harga}\n")
                    }
                }

                append("-----------------------------\n")
                append("Subtotal     : ${tvSubtotalTambahan.text}\n")
                append("TOTAL BAYAR  : ${tvTotalBayar.text}\n")
                append("============================\n")
                append("Terima kasih!\n\n")
            }.toString()

            printToBluetooth(message)

        }
    }

    private fun setupWhatsappButton() {
        btnKirimWhatsapp.setOnClickListener {
            val message = StringBuilder().apply {
                append("*Hai Halo* ${tvNamaPelanggan.text} 👋\n\n")
                append("*Berikut rincian laundry Anda:*\n")
                append("• Layanan Utama: ${tvLayananUtama.text}\n")
                append("• Harga: ${tvHargaLayanan.text}\n\n")
                append("*Rincian Tambahan:*\n")
                listTambahan.forEachIndexed { i, item ->
                    append("${i + 1}. ${item.namaTambahan} - ${formatRupiah(extractHargaFromString(   item.harga ?: "0"))}\n")
                }
                append("\n*Total Bayar:* ${tvTotalBayar.text}\n\n")
                append("Terima kasih telah menggunakan layanan Floppy Laundry 💟")
            }.toString()

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/?text=" + Uri.encode(message))
            startActivity(intent)
        }
    }

    private fun setupKembaliButton() {
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }


    private fun printToBluetooth(text: String) {
        Thread {
            try {
                if (!isBluetoothReady()) return@Thread

                val device = bluetoothAdapter?.getRemoteDevice(printerMAC)
                if (device != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
                    ) {
                        runOnUiThread {
                            Toast.makeText(this, getString(R.string.izin), Toast.LENGTH_SHORT).show()
                        }
                        return@Thread
                    }

                    bluetoothSocket = device.createRfcommSocketToServiceRecord(printerUUID)
                    bluetoothSocket?.connect()
                    outputStream = bluetoothSocket?.outputStream

                    outputStream?.use {
                        it.write(text.toByteArray())
                        it.flush()
                    }

                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.cetak), Toast.LENGTH_SHORT).show()
                    }

                    bluetoothSocket?.close()
                } else {
                    runOnUiThread {
                        Toast.makeText(this, getString(R.string.tidak_ditemukan), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.gagal_cetak, e.message), Toast.LENGTH_LONG).show()
                }
                e.printStackTrace()
            }
        }.start()
    }


    private fun isBluetoothReady(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED
        ) {
            runOnUiThread {
                Toast.makeText(this, getString(R.string.izin_cetak), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            runOnUiThread {
                Toast.makeText(this, getString(R.string.tidak_blou), Toast.LENGTH_SHORT).show()
            }
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.blu), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.blu_tolak), Toast.LENGTH_SHORT).show()
        }
    }

}