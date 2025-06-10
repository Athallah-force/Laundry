package com.athalah.laundry.model_data

data class model_laporan(
    var idTransaksi: String? = null,
    var namaPelanggan: String? = null,
    var tanggal: String? = null,
    var layananUtama: String? = null,
    var tambahan: List<String>? = null,
    var totalBayar: String? = null,
    var estimasi: String? = null,
    var metodePembayaran: String? = null,
    var status: String? = null,
    var status_pembayaran: String? = null,
    var status_pengambilan: String? = null,
    var waktuDiambil: String? = null

) {
    constructor() : this(
        null, null, null, null, null, null, null, null, null, null, null
    )

}
