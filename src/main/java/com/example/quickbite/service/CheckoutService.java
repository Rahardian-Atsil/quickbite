package com.example.quickbite.service;

import com.example.quickbite.dto.request.CheckoutRequest;
import com.example.quickbite.dto.response.CheckoutResponse;

public interface CheckoutService {
    CheckoutResponse prosesCheckout(CheckoutRequest request);
    void batalkanTransaksi(String idPesanan, String alasan);
}