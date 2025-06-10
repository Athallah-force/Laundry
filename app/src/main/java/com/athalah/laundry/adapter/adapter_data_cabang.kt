package com.athalah.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_cabang

class adapter_data_cabang (
    private val listCabang: ArrayList<model_cabang>) :
        RecyclerView.Adapter<adapter_data_cabang.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_cabang, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listCabang[position]
        holder.idCabang.text = item.idCabang?: "-"
        holder.namaCabang.text = item.namaCabang
        holder.alamat.text = item.alamat

        holder.card_cabang.setOnClickListener(){

        }

    }

    override fun getItemCount(): Int {
        return listCabang.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_cabang: CardView =  itemView.findViewById(R.id.card_cabang)
        val idCabang: TextView = itemView.findViewById(R.id.tv_id_cabang)
        val namaCabang: TextView= itemView.findViewById(R.id.tv_nama_cabang)
        val alamat: TextView = itemView.findViewById(R.id.tv_alamat)

    }
}