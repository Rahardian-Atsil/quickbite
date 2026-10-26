package com.example.quickbite.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequest {

    @NotBlank(message = "ID Pelanggan wajib diisi")
    private String idPelanggan;

    @NotEmpty(message = "Keranjang belanja tidak boleh kosong")
    @Valid
    private List<CheckoutItemRequest> keranjangData;

    @NotBlank(message = "Metode pembayaran wajib diisi (contoh: CREDIT_CARD)")
    private String metodePembayaran;

    @NotBlank(message = "Token kartu/kredensial pembayaran wajib diisi")
    private String tokenKartu;
}