package com.athalah.laundry.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_layanan
import com.athalah.laundry.Transaksi
import com.google.firebase.database.DatabaseReference

class adapter_pilih_layanan (private val layananList: ArrayList<model_layanan>) : RecyclerView.Adapter<adapter_pilih_layanan.ViewHolder>() {
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pilih_layanan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_pilih_layanan.ViewHolder, position: Int) {
        var nomor = position + 1
        val item = layananList[position]
        holder.tvID.text = "[$nomor]"
        holder.tv_nama.text = item.namaLayanan
        holder.tv_harga.text = item.harga
        holder.cvCard.setOnClickListener() {
            val intent = Intent(holder.itemView.context, Transaksi::class.java)
            intent.putExtra("id", item.idLayanan)
            intent.putExtra("nama", item.namaLayanan)
            intent.putExtra("harga", item.harga)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }
    override fun getItemCount(): Int {
        return layananList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCard: CardView =  itemView.findViewById(R.id.card_layanan)
        val tvID: TextView =  itemView.findViewById(R.id.tv_id_layanan)
        val tv_nama: TextView= itemView.findViewById(R.id.tv_nama_layanan)
        val tv_harga: TextView = itemView.findViewById(R.id.tv_harga)
    }
}