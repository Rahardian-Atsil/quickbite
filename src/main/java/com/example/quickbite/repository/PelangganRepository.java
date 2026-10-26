package com.example.quickbite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quickbite.entity.Pelanggan;

public interface PelangganRepository extends JpaRepository<Pelanggan, String> {
}