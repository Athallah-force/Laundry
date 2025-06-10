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
import com.athalah.laundry.Transaksi
import com.athalah.laundry.model_data.model_tambahkan
import com.google.firebase.database.DatabaseReference

class adapter_pilih_tambahan (private val tambahanList: ArrayList<model_tambahkan>) : RecyclerView.Adapter<adapter_pilih_tambahan.ViewHolder>() {
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pilih_tambahan, parent, false)
        appContext = parent.context
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var nomor = position + 1
        val item = tambahanList[position]
        holder.tvID.text = "[$nomor]"
        holder.tv_nama.text = item.namaTambahan
        holder.tv_harga.text = item.harga
        holder.cvCard.setOnClickListener() {
            val resultIntent = Intent()
            resultIntent.putExtra("id", item.idTambahan)
            resultIntent.putExtra("nama", item.namaTambahan)
            resultIntent.putExtra("harga", item.harga)
            (holder.itemView.context as Activity).setResult(Activity.RESULT_OK, resultIntent)
            (holder.itemView.context as Activity).finish()
        }
    }
    override fun getItemCount(): Int {
        return tambahanList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCard: CardView =  itemView.findViewById(R.id.card_tambahan)
        val tvID: TextView =  itemView.findViewById(R.id.tv_id_tambahkan)
        val tv_nama: TextView= itemView.findViewById(R.id.tv_nama_tambahkan)
        val tv_harga: TextView = itemView.findViewById(R.id.tv_harga)
    }


}