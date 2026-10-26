package com.example.quickbite.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutItemRequest {

    @NotBlank(message = "ID Menu tidak boleh kosong")
    private String idMenu;

    @NotNull(message = "Jumlah tidak boleh null")
    @Min(value = 1, message = "Jumlah pembelian minimal 1")
    private Integer jumlah;
}