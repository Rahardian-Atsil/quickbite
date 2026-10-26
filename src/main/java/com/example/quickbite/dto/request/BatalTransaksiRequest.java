package com.example.quickbite.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatalTransaksiRequest {

    @NotBlank(message = "Alasan pembatalan harus disertakan")
    private String alasan;
}