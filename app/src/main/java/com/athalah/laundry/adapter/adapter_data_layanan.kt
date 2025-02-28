package com.athalah.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_layanan

class  adapter_data_layanan(
    private val listLayanan: ArrayList<model_layanan>) :
    RecyclerView.Adapter<adapter_data_layanan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.idLayanan.text = item.idLayanan ?: "-"
        holder.namaLayanan.text = item.namaLayanan
        holder.harga.text = item.harga
        holder.namaCabang.text = item.namaCabang

        holder.card_layanan.setOnClickListener(){

        }
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_layanan: CardView =  itemView.findViewById(R.id.card_layanan)
        val idLayanan: TextView= itemView.findViewById(R.id.tv_id_layanan)
        val namaLayanan: TextView= itemView.findViewById(R.id.tv_nama_layanan)
        val harga: TextView = itemView.findViewById(R.id.tv_harga)
        val namaCabang: TextView = itemView.findViewById(R.id.tv_nama_cabang)

    }

}