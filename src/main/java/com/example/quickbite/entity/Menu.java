package com.example.quickbite.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @Column(name = "id_menu", length = 36)
    private String idMenu;

    @Column(name = "nama_menu", nullable = false, length = 100)
    private String namaMenu;

    @Column(nullable = false)
    private double harga;

    @Column(nullable = false)
    private int stok;

    // Domain Methods sesuai Class Diagram
    public boolean validasiStok(int qty) {
        return this.stok >= qty && qty > 0;
    }

    public boolean kurangStok(int qty) {
        if (!validasiStok(qty)) {
            return false;
        }
        this.stok -= qty;
        return true;
    }

    public double getHarga() {
        return this.harga;
    }
}