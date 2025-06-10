package com.athalah.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R

import com.athalah.laundry.model_data.model_tambahkan

class adapter_konfirmasi_tambahan(private val list: List<model_tambahkan>) :
    RecyclerView.Adapter<adapter_konfirmasi_tambahan.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNama: TextView = view.findViewById(R.id.textNamaTambahan)
        val textHarga: TextView = view.findViewById(R.id.textHargaTambahan)
        val textIndex: TextView = view.findViewById(R.id.textIndexTambahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_konfirmasi_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textNama.text = item.namaTambahan
        holder.textHarga.text = "Rp${item.harga},00"
        holder.textIndex.text = "[${position + 1}]"
    }

    override fun getItemCount(): Int = list.size
}
