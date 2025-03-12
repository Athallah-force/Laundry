package com.athalah.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_pegawai
import pegawai.tambah_pegawai_Activity

class  adapter_data_pegawai(private val listPegawai: ArrayList<model_pegawai>) :

    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
        lateinit var appContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPegawai[position]
        holder.tvID.text = item.id
        holder.tv_nama.text = item.nama
        holder.tv_alamat.text = item.alamat
        holder.tv_cabang.text = item.cabang
        holder.tv_terdaftar.text = item.terdaftar
        holder.tv_nohp.text = item.nohp

        holder.bt_hubungi.setOnClickListener(){

        }
        holder.bt_lihat.setOnClickListener(){

        }
        holder.cvCard.setOnClickListener(){
            val intent = Intent(holder.itemView.context, tambah_pegawai_Activity::class.java)
            intent.putExtra("judul", "edit")
            intent.putExtra("id", item.id)
            intent.putExtra("nama", item.nama)
            intent.putExtra("alamat", item.alamat)
            intent.putExtra("cabang", item.cabang)
            intent.putExtra("terdaftar", item.terdaftar)
            intent.putExtra("nohp", item.nohp)
            appContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCard: View = itemView.findViewById(R.id.main)
        val tvID: TextView =  itemView.findViewById(R.id.tv_card_pegawai_id)
        val tv_nama: TextView= itemView.findViewById(R.id.tv_nama)
        val tv_alamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val tv_cabang: TextView = itemView.findViewById(R.id.tv_cabang)
        val tv_terdaftar: TextView = itemView.findViewById(R.id.tv_terdaftar)
        val tv_nohp: TextView = itemView.findViewById(R.id.tv_nohp)
        val bt_hubungi: Button = itemView.findViewById(R.id.bt_hubungi)
        val bt_lihat: Button = itemView.findViewById(R.id.bt_lihat)

    }

}