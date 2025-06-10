package com.athalah.laundry.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_laporan
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class adapter_data_laporan(
    private val context: Context,
    laporanList: List<model_laporan>,
) : RecyclerView.Adapter<adapter_data_laporan.LaporanViewHolder>() {

    private var listLaporan: List<model_laporan> = laporanList
    private var fullList: List<model_laporan> = laporanList

    inner class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvJenisLayanan: TextView = itemView.findViewById(R.id.tvJenisLayanan)
        val tvLayananTambahan: TextView = itemView.findViewById(R.id.tvLayananTambahan)
        val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        val btnBayar: Button = itemView.findViewById(R.id.btnBayar)
        val tvWaktuDiambil: TextView = itemView.findViewById(R.id.tvWaktuDiambil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_laporan, parent, false)
        return LaporanViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        val laporan = listLaporan[position]

        holder.tvNama.text = laporan.namaPelanggan
        holder.tvTanggal.text = laporan.tanggal
        holder.tvJenisLayanan.text = laporan.layananUtama

        val tambahanList = laporan.tambahan?.filterNotNull() ?: emptyList()
        val tambahanCount = tambahanList.size
        val tambahanText = when {
            tambahanCount == 0 -> context.getString(R.string.tidak_ada)
            tambahanCount == 1 -> context.getString(R.string.satu_tambahan, tambahanList[0])
            else -> context.getString(R.string.tambahan_lainnya, tambahanList[0], tambahanCount - 1)
        }
        holder.tvLayananTambahan.text = tambahanText
        holder.tvTotal.text = laporan.totalBayar?.toString() ?: "0"

        if (laporan.status.equals("Lunas", ignoreCase = true) && !laporan.waktuDiambil.isNullOrEmpty()) {
            holder.tvWaktuDiambil.visibility = View.VISIBLE
            holder.tvWaktuDiambil.text = context.getString(R.string.diambil_pada, laporan.waktuDiambil)
        } else {
            holder.tvWaktuDiambil.visibility = View.GONE
        }

        when (laporan.status) {
            "Belum Dibayar" -> {
                holder.tvStatus.text = context.getString(R.string.belum_dibayar)
                holder.tvStatus.setBackgroundColor(context.getColor(R.color.red))
                holder.tvStatus.setTextColor(context.getColor(R.color.white))
                holder.btnBayar.text = context.getString(R.string.bayar)
                holder.btnBayar.setBackgroundColor(context.getColor(R.color.red))
                holder.btnBayar.visibility = View.VISIBLE

                holder.btnBayar.setOnClickListener {
                    laporan.idTransaksi?.let { id -> showMetodePembayaran(context, id, position) }
                }
            }

            "Belum Diambil" -> {
                holder.tvStatus.text = context.getString(R.string.belum_diambil)
                holder.tvStatus.setBackgroundColor(context.getColor(R.color.yellow))
                holder.tvStatus.setTextColor(context.getColor(R.color.black))
                holder.btnBayar.text = context.getString(R.string.pesanan_diambil)
                holder.btnBayar.setBackgroundColor(context.getColor(R.color.yellow))
                holder.btnBayar.visibility = View.VISIBLE

                holder.btnBayar.setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("Konfirmasi")
                        .setMessage("Yakin pesanan sudah diambil?")
                        .setPositiveButton("Ya") { _, _ ->
                            laporan.idTransaksi?.let { id -> updatePesananDiambil(id, position) }
                        }
                        .setNegativeButton("Batal", null)
                        .show()
                }
            }

            "Lunas" -> {
                holder.tvStatus.text = context.getString(R.string.selesai)
                holder.tvStatus.setBackgroundColor(context.getColor(R.color.green))
                holder.tvStatus.setTextColor(context.getColor(R.color.white))
                holder.btnBayar.visibility = View.GONE

                if (!laporan.waktuDiambil.isNullOrEmpty()) {
                    holder.tvWaktuDiambil.visibility = View.VISIBLE
                    holder.tvWaktuDiambil.text = context.getString(R.string.diambil_pada, laporan.waktuDiambil)
                } else {
                    holder.tvWaktuDiambil.visibility = View.GONE
                }
            }

            else -> {
                holder.tvStatus.text = laporan.status
                holder.btnBayar.visibility = View.GONE
                holder.tvWaktuDiambil.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = listLaporan.size

    fun filter(query: String) {
        listLaporan = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.namaPelanggan?.contains(query, ignoreCase = true) == true ||
                        it.tanggal?.contains(query, ignoreCase = true) == true ||
                        it.status?.contains(query, ignoreCase = true) == true ||
                        it.layananUtama?.contains(query, ignoreCase = true) == true
            }
        }
        notifyDataSetChanged()
    }


    private fun showMetodePembayaran(context: Context, transaksiId: String, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_metode_pembayaran_laporan, null)
        val dialog = AlertDialog.Builder(context).setView(dialogView).create()

        val onClick: (String) -> Unit = { metode ->
            updateStatusPembayaran(transaksiId, metode, position)
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.buttonTunai).setOnClickListener { onClick("Tunai") }
        dialogView.findViewById<Button>(R.id.buttonQRIS).setOnClickListener { onClick("QRIS") }
        dialogView.findViewById<Button>(R.id.buttonDANA).setOnClickListener { onClick("DANA") }
        dialogView.findViewById<Button>(R.id.buttonGoPay).setOnClickListener { onClick("GoPay") }
        dialogView.findViewById<Button>(R.id.buttonOVO).setOnClickListener { onClick("OVO") }
        dialogView.findViewById<TextView>(R.id.buttonBatal).setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun updateStatusPembayaran(transaksiId: String, metode: String, position: Int) {
        val ref = FirebaseDatabase.getInstance().getReference("laporan").child(transaksiId)
        val updates = mapOf(
            "metodePembayaran" to metode,
            "status" to "Belum Diambil"
        )

        ref.updateChildren(updates).addOnSuccessListener {
            val laporan = listLaporan[position]
            laporan.metodePembayaran = metode
            laporan.status = "Belum Diambil"

            notifyItemChanged(position)
            Toast.makeText(context, context.getString(R.string.pembayaran_berhasil, metode), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.gagal_perbarui), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePesananDiambil(transaksiId: String, position: Int) {
        val waktuDiambil = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date())
        val laporan = listLaporan[position]
        val totalBayar = laporan.totalBayar ?: 0L

        val ref = FirebaseDatabase.getInstance().getReference("laporan").child(transaksiId)
        val updates = mapOf(
            "status" to "Lunas",
            "waktuDiambil" to waktuDiambil,
            "estimasi" to totalBayar
        )

        ref.updateChildren(updates).addOnSuccessListener {
            laporan.status = "Lunas"
            laporan.waktuDiambil = waktuDiambil

            notifyItemChanged(position)
            Toast.makeText(context, context.getString(R.string.pesanan_diambil), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, context.getString(R.string.gagal_status), Toast.LENGTH_SHORT).show()
        }
    }
}
