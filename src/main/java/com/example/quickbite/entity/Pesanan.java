package com.example.quickbite.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pesanan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pesanan {

    @Id
    @Column(name = "id_pesanan", length = 36)
    private String idPesanan;

    @Column(nullable = false)
    private LocalDateTime tanggal;

    @Column(name = "total_biaya", nullable = false)
    private double totalBiaya;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPesanan status;

    // Relasi ke Pelanggan
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pelanggan", nullable = false)
    private Pelanggan pelanggan;

    // Komposisi mutlak: ItemPesanan terikat sepenuhnya pada siklus hidup Pesanan
    @OneToMany(mappedBy = "pesanan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemPesanan> daftarItem = new ArrayList<>();

    // Relasi 0..1 ke Pembayaran (dilunasi dengan)
    @OneToOne(mappedBy = "pesanan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pembayaran pembayaran;

    // Domain Methods sesuai Class Diagram
    public void tambahItem(ItemPesanan item) {
        if (item != null) {
            this.daftarItem.add(item);
            item.setPesanan(this);
            item.hitungSubtotal();
            hitungTotal();
        }
    }

    public double hitungTotal() {
        this.totalBiaya = this.daftarItem.stream()
                .mapToDouble(ItemPesanan::hitungSubtotal)
                .sum();
        return this.totalBiaya;
    }

    public void updateStatus(StatusPesanan statusBaru) {
        this.status = statusBaru;
    }
}