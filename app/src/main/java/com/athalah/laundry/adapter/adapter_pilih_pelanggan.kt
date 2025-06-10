package com.athalah.laundry.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.athalah.laundry.R
import com.athalah.laundry.model_data.model_pelanggan
import com.athalah.laundry.Transaksi

class adapter_pilih_pelanggan(private val pelangganList: ArrayList<model_pelanggan>) : RecyclerView.Adapter<adapter_pilih_pelanggan.ViewHolder>() {
    lateinit var appContext: Context
    private val TAG = "adapter_pilih_pelanggan"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pilih_pelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = pelangganList[position]

        Log.d(TAG, "onBindViewHolder position $position: ${item.namaPelanggan}")

        holder.tvID.text = "[$nomor]"
        holder.tv_nama.text = item.namaPelanggan
        holder.tv_alamat.text = item.alamatPelanggan
        holder.tv_nohp.text = item.noHPPelanggan

        holder.cvCard.setOnClickListener {
            val intent = Intent(holder.itemView.context, Transaksi::class.java)
            intent.putExtra("id", item.idPelanggan)
            intent.putExtra("nama", item.namaPelanggan)
            intent.putExtra("alamat", item.alamatPelanggan)
            intent.putExtra("nohp", item.noHPPelanggan)
            (appContext as Activity).setResult(Activity.RESULT_OK, intent)
            (appContext as Activity).finish()
        }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${pelangganList.size}")
        return pelangganList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCard: CardView = itemView.findViewById(R.id.cv_card_pelanggan)
        val tvID: TextView = itemView.findViewById(R.id.tv_card_pelanggan_id)
        val tv_nama: TextView = itemView.findViewById(R.id.tv_nama)
        val tv_alamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val tv_nohp: TextView = itemView.findViewById(R.id.tv_nohp)
    }
}