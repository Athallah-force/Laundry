package com.athalah.laundry.adapter

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

class  adapter_data_pelanggan(private val listPelanggan: ArrayList<model_pelanggan>) :

    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
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

        }
        holder.bt_lihat.setOnClickListener(){

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

}