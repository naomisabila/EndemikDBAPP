package com.example.endemikdbapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Endemik implements Serializable {
    private String id;
    private String nama;
    
    @SerializedName("tipe")
    private String kategori;
    
    @SerializedName("foto")
    private String gambar;
    
    private String deskripsi;
    
    @SerializedName("nama_latin")
    private String namaLatin;
    
    private String asal;
    private String sebaran;
    private String status;
    private String famili;
    private String genus;

    public Endemik() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getNamaLatin() { return namaLatin; }
    public void setNamaLatin(String namaLatin) { this.namaLatin = namaLatin; }

    public String getAsal() { return asal; }
    public void setAsal(String asal) { this.asal = asal; }

    public String getSebaran() { return sebaran; }
    public void setSebaran(String sebaran) { this.sebaran = sebaran; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFamili() { return famili; }
    public void setFamili(String famili) { this.famili = famili; }

    public String getGenus() { return genus; }
    public void setGenus(String genus) { this.genus = genus; }
}