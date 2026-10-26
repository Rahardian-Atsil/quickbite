package com.example.quickbite.repository;

import com.example.quickbite.entity.ItemPesanan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPesananRepository extends JpaRepository<ItemPesanan, String> {
}