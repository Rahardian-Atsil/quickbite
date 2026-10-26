package com.example.quickbite.repository;

import com.example.quickbite.entity.Pembayaran;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PembayaranRepository extends JpaRepository<Pembayaran, String> {
}