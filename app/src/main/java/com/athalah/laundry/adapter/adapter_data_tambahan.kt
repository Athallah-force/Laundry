package com.athalah.laundry.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_tambahkan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class adapter_data_tambahan (
    private val listTambahkan: ArrayList<model_tambahkan>) :
        RecyclerView.Adapter<adapter_data_tambahan.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_tambahkan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTambahkan[position]
        holder.idTambahan.text = item.idTambahan ?: "-"
        holder.namaTambahan.text = item.namaTambahan
        holder.harga.text = item.harga

        holder.card_tambahkan.setOnClickListener(){

        }

    }

    override fun getItemCount(): Int {
        return listTambahkan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_tambahkan: CardView =  itemView.findViewById(R.id.card_tambahkan)
        val idTambahan: TextView = itemView.findViewById(R.id.tv_id_tambahkan)
        val namaTambahan: TextView= itemView.findViewById(R.id.tv_nama_tambahkan)
        val harga: TextView = itemView.findViewById(R.id.tv_harga)

    }


}