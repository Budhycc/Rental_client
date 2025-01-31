package org.rental.main.Transaksi;

import org.rental.main.Mobil.MobilData;
import org.rental.main.Pelanggan.PelangganData;

public class TransaksiData {
    private int nomor;
    private int idTransaksi;
    private PelangganData pelanggan;
    private MobilData mobil;
    private String tanggalMulai;
    private String tanggalSelesai;
    private String totalHarga;
    private String denda;
    private String status;

    // Getter dan Setter
    public int getNomor() {
        return nomor;
    }

    public void setNomor(int nomor) {
        this.nomor = nomor;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public PelangganData getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(PelangganData pelanggan) {
        this.pelanggan = pelanggan;
    }

    public MobilData getMobil() {
        return mobil;
    }

    public void setMobil(MobilData mobil) {
        this.mobil = mobil;
    }

    public String getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(String tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(String tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(String totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getDenda() {
        return denda;
    }

    public void setDenda(String denda) {
        this.denda = denda;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
