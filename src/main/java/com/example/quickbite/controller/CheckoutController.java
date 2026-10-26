package com.example.quickbite.controller;

import com.example.quickbite.dto.request.BatalTransaksiRequest;
import com.example.quickbite.dto.request.CheckoutRequest;
import com.example.quickbite.dto.response.ApiResponse;
import com.example.quickbite.dto.response.CheckoutResponse;
import com.example.quickbite.entity.Menu;
import com.example.quickbite.repository.MenuRepository;
import com.example.quickbite.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final MenuRepository menuRepository;

    /**
     * Endpoint tambahan untuk menampilkan daftar menu & sisa stok di Web UI
     */
    @GetMapping("/menus")
    public ResponseEntity<ApiResponse<List<Menu>>> getMenus() {
        return ResponseEntity.ok(ApiResponse.success("Daftar menu berhasil diambil", menuRepository.findAll()));
    }

    /**
     * Merepresentasikan method: +prosesCheckout(idPelanggan, keranjangData)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CheckoutResponse>> prosesCheckout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        CheckoutResponse response = checkoutService.prosesCheckout(request);
        return new ResponseEntity<>(
                ApiResponse.success("Checkout dan pembayaran berhasil diproses", response),
                HttpStatus.CREATED
        );
    }

    /**
     * Merepresentasikan method: +batalkanTransaksi(alasan)
     */
    @PostMapping("/{idPesanan}/batal")
    public ResponseEntity<ApiResponse<Void>> batalkanTransaksi(
            @PathVariable String idPesanan,
            @Valid @RequestBody BatalTransaksiRequest request
    ) {
        checkoutService.batalkanTransaksi(idPesanan, request.getAlasan());
        return ResponseEntity.ok(ApiResponse.success("Transaksi berhasil dibatalkan: " + request.getAlasan(), null));
    }
}