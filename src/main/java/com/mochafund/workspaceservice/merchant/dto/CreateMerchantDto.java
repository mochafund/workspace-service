package com.mochafund.workspaceservice.merchant.dto;

import com.mochafund.workspaceservice.merchant.entity.Merchant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMerchantDto {

    @NotBlank(message = "Merchant payee must be provided")
    @Size(min = 1, max = 100, message = "Merchant payee must be between 1 and 100 characters")
    private String payee;

    public static Merchant fromDto(CreateMerchantDto merchantDto) {
        return Merchant.builder()
                .payee(merchantDto.getPayee())
                .build();
    }
}
