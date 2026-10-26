package com.example.quickbite;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.quickbite.controller.CheckoutController;
import com.example.quickbite.dto.request.CheckoutRequest;
import com.example.quickbite.dto.response.CheckoutResponse;
import com.example.quickbite.entity.StatusPesanan;
import com.example.quickbite.repository.MenuRepository;
import com.example.quickbite.service.CheckoutService;

@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private CheckoutController checkoutController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();
    }

    @Test
    @DisplayName("Uji boundary checkout berhasil mengembalikan status CREATED")
    void testProsesCheckoutSukses() throws Exception {
        CheckoutResponse mockRes = CheckoutResponse.builder()
                .idPesanan("ORD-TEST-01")
                .idPembayaran("PAY-TEST-01")
                .totalBiaya(50000.0)
                .statusPesanan(StatusPesanan.DIPROSES)
                .statusPembayaran("SUCCESS")
                .kodeReferensi("PG-REF-TEST")
                .tanggal(LocalDateTime.now())
                .build();

        Mockito.when(checkoutService.prosesCheckout(any(CheckoutRequest.class)))
                .thenReturn(mockRes);

        String jsonPayload = """
                {
                  "idPelanggan": "CUST-001",
                  "metodePembayaran": "CREDIT_CARD",
                  "tokenKartu": "tok_visa_valid_999",
                  "keranjangData": [
                    { "idMenu": "MENU-01", "jumlah": 2 }
                  ]
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.idPesanan").value("ORD-TEST-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalBiaya").value(50000.0));
    }
}