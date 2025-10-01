package com.mochafund.workspaceservice.merchant.dto;

import com.mochafund.workspaceservice.tag.enums.TagStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMerchantDto {

    @Size(min = 1, max = 100, message = "Merchant payee must be between 1 and 100 characters")
    private String payee;

    private TagStatus status;

}
