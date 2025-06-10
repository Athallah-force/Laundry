package com.athalah.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_pelanggan
import pelanggan.data_pelanggan_Activity
import pelanggan.tambah_pelanggan_Activity

class  adapter_data_pelanggan(private val listPelanggan: ArrayList<model_pelanggan>) :

    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {
        lateinit var appContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPelanggan[position]
        holder.tvID.text = item.idPelanggan
        holder.tv_nama.text = item.namaPelanggan
        holder.tv_alamat.text = item.alamatPelanggan
        holder.tv_terdaftar.text = item.terdaftar
        holder.tv_nohp.text = item.noHPPelanggan
        holder.cvCard.setOnClickListener(){

        }
        holder.bt_hubungi.setOnClickListener(){
            val nomorHp = item.noHPPelanggan
            nomorHp?.let { it1 -> showHubungiDialog(holder.itemView.context, it1) }

        }
        holder.bt_lihat.setOnClickListener(){

        }
        holder.cvCard.setOnClickListener(){
            val intent = Intent(holder.itemView.context, tambah_pelanggan_Activity::class.java)
            intent.putExtra("judul", "edit")
            intent.putExtra("id", item.idPelanggan)
            intent.putExtra("nama", item.namaPelanggan)
            intent.putExtra("alamat", item.alamatPelanggan)
            intent.putExtra("terdaftar", item.terdaftar)
            intent.putExtra("nohp", item.noHPPelanggan)
            appContext.startActivity(intent)

        }

        holder.bt_lihat.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.dialog_mod_pelanggan, null)

            val dialog = android.app.AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .create()

            // Set data ke TextView di dialog
            dialogView.findViewById<TextView>(R.id.tvjudul2).text = item.idPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul4).text = item.namaPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul6).text = item.alamatPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul8).text = item.noHPPelanggan
            dialogView.findViewById<TextView>(R.id.tvjudul12).text = item.terdaftar

            // Tombol Edit
            dialogView.findViewById<Button>(R.id.buttonsunting).setOnClickListener {
                val intent = Intent(holder.itemView.context, tambah_pelanggan_Activity::class.java)
                intent.putExtra("judul", "edit")
                intent.putExtra("id", item.idPelanggan)
                intent.putExtra("nama", item.namaPelanggan)
                intent.putExtra("alamat", item.alamatPelanggan)
                intent.putExtra("terdaftar", item.terdaftar)
                intent.putExtra("nohp", item.noHPPelanggan)
                holder.itemView.context.startActivity(intent)
                dialog.dismiss()
            }

            // Tombol Hapus
            dialogView.findViewById<Button>(R.id.buttonhapus).setOnClickListener {
                android.app.AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Hapus pelanggan?")
                    .setMessage("Data pelanggan akan dihapus.")
                    .setPositiveButton("Hapus") { _, _ ->
                        // Hapus dari Firebase
                        val db = com.google.firebase.database.FirebaseDatabase.getInstance()
                            .getReference("pelanggan")
                        item.idPelanggan?.let { it1 ->
                            db.child(it1).removeValue().addOnSuccessListener {
                                android.widget.Toast.makeText(holder.itemView.context, "Data dihapus", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }

            dialog.show()
        }

    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCard: CardView =  itemView.findViewById(R.id.cv_card_pelanggan)
        val tvID: TextView =  itemView.findViewById(R.id.tv_card_pelanggan_id)
        val tv_nama: TextView= itemView.findViewById(R.id.tv_nama)
        val tv_alamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val tv_terdaftar: TextView = itemView.findViewById(R.id.tv_terdaftar)
        val tv_nohp: TextView = itemView.findViewById(R.id.tv_nohp)
        val bt_hubungi: Button = itemView.findViewById(R.id.bt_hubungi)
        val bt_lihat: Button = itemView.findViewById(R.id.bt_lihat)

    }

    private fun showHubungiDialog(context: Context, phoneNumber: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_hubungi_pelanggan, null)
        val dialog = android.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        val btnWhatsapp = dialogView.findViewById<Button>(R.id.btnWhatsapp)
        val btnTelepon = dialogView.findViewById<Button>(R.id.btnTelepon)
        val btnBatal = dialogView.findViewById<Button>(R.id.btnBatal)

        btnWhatsapp.setOnClickListener {
            val formattedNumber = if (phoneNumber.startsWith("0")) {
                "+62" + phoneNumber.substring(1)
            } else {
                phoneNumber
            }
            val url = "https://wa.me/$formattedNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            context.startActivity(intent)
            dialog.dismiss()

        }
        btnTelepon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = android.net.Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

}