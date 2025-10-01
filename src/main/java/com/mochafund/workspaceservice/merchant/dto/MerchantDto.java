package com.mochafund.workspaceservice.merchant.dto;

import com.mochafund.workspaceservice.common.dto.BaseDto;
import com.mochafund.workspaceservice.merchant.entity.Merchant;
import com.mochafund.workspaceservice.merchant.enums.MerchantStatus;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MerchantDto extends BaseDto {

    private UUID workspaceId;
    private UUID createdBy;
    private String payee;
    private MerchantStatus status;

    public static MerchantDto fromEntity(Merchant merchant) {
        return MerchantDto.builder()
                .id(merchant.getId())
                .createdAt(merchant.getCreatedAt())
                .updatedAt(merchant.getUpdatedAt())
                .workspaceId(merchant.getWorkspaceId())
                .createdBy(merchant.getCreatedBy())
                .payee(merchant.getPayee())
                .status(merchant.getStatus())
                .build();
    }

    public static List<MerchantDto> fromEntities(List<Merchant> merchants) {
        return merchants.stream().map(MerchantDto::fromEntity).toList();
    }
}
