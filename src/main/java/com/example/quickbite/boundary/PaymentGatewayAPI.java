package com.example.quickbite.boundary;

public interface PaymentGatewayAPI {
    String mintaTransaksiPembayaran(double total, String tokenKartu);
}