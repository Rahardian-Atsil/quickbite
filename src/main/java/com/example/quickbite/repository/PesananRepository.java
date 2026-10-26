package com.example.quickbite.repository;

import com.example.quickbite.entity.Pesanan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PesananRepository extends JpaRepository<Pesanan, String> {
}