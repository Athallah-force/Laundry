package com.athalah.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_tambahkan

class adapter_tambahan_dipilih(
    private val list: MutableList<model_tambahkan>,
    private val onDeleteClick: (model_tambahkan) -> Unit
) : RecyclerView.Adapter<adapter_tambahan_dipilih.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_tambahkan)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
        val tvID: TextView = itemView.findViewById(R.id.tv_id_tambahkan)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_hapus_tambahan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tambahan_dipilih, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tambahan = list[position]
        holder.tvID.text = "[${position + 1}]"
        holder.tvNama.text = tambahan.namaTambahan
        holder.tvHarga.text = "Harga : Rp${tambahan.harga}"
        holder.btnDelete.setOnClickListener {
            onDeleteClick(tambahan)
        }
    }
}
