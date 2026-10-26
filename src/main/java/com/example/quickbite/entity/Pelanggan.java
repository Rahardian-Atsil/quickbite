package com.example.quickbite.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pelanggan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pelanggan {

    @Id
    @Column(name = "id_pelanggan", length = 36)
    private String idPelanggan;

    @Column(nullable = false, length = 100)
    private String nama;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "nomor_telepon", length = 20)
    private String nomorTelepon;

    @Column(columnDefinition = "TEXT")
    private String alamat;

    @OneToMany(mappedBy = "pelanggan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Pesanan> daftarPesanan = new ArrayList<>();

    // Domain Methods sesuai Class Diagram
    public String getInfoPelanggan() {
        return String.format("%s (%s) - %s", this.nama, this.email, this.nomorTelepon);
    }

    public void ubahAlamat(String alamatBaru) {
        if (alamatBaru == null || alamatBaru.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat baru tidak boleh kosong");
        }
        this.alamat = alamatBaru;
    }
}