package com.athalah.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_tambahkan

class adapter_data_tambahkan(
    private val list: ArrayList<model_tambahkan>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<adapter_data_tambahkan.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_hapus_tambahan)
        val tvID: TextView = itemView.findViewById(R.id.tv_id_tambahkan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tambahan_dipilih, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvID.text = "[${position + 1}]"
        holder.tvNama.text = item.namaTambahan
        holder.tvHarga.text = "Rp${item.harga}"
        holder.btnDelete.setOnClickListener {
            onDelete(position)
        }
    }

    override fun getItemCount() = list.size
}
