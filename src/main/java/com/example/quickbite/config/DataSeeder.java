package com.example.quickbite.config;

import com.example.quickbite.entity.Menu;
import com.example.quickbite.entity.Pelanggan;
import com.example.quickbite.repository.MenuRepository;
import com.example.quickbite.repository.PelangganRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final PelangganRepository pelangganRepository;
    private final MenuRepository menuRepository;

    @Override
    public void run(String... args) {
        if (!pelangganRepository.existsById("CUST-001")) {
            pelangganRepository.save(Pelanggan.builder()
                    .idPelanggan("CUST-001")
                    .nama("Budi Santoso")
                    .email("budi@example.com")
                    .nomorTelepon("08123456789")
                    .alamat("Jl. Sudirman No. 45, Jakarta")
                    .build());
            System.out.println("== [SEEDER] Data Pelanggan CUST-001 berhasil ditambahkan ==");
        }

        if (!menuRepository.existsById("MENU-01")) {
            menuRepository.save(Menu.builder()
                    .idMenu("MENU-01")
                    .namaMenu("Kopi Susu Gula Aren")
                    .harga(18000.0)
                    .stok(20)
                    .build());
            System.out.println("== [SEEDER] Data Menu MENU-01 berhasil ditambahkan ==");
        }

        if (!menuRepository.existsById("MENU-02")) {
            menuRepository.save(Menu.builder()
                    .idMenu("MENU-02")
                    .namaMenu("Croissant Butter")
                    .harga(25000.0)
                    .stok(10)
                    .build());
            System.out.println("== [SEEDER] Data Menu MENU-02 berhasil ditambahkan ==");
        }
    }
}