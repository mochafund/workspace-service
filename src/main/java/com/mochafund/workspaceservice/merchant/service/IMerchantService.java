package com.mochafund.workspaceservice.merchant.service;

import com.mochafund.workspaceservice.merchant.dto.CreateMerchantDto;
import com.mochafund.workspaceservice.merchant.dto.UpdateMerchantDto;
import com.mochafund.workspaceservice.merchant.entity.Merchant;

import java.util.List;
import java.util.UUID;

public interface IMerchantService {
    Merchant createMerchant(UUID userId, UUID workspaceId, CreateMerchantDto merchantDto);
    Merchant getMerchant(UUID workspaceId, UUID merchantId);
    Merchant updateMerchant(UUID workspaceId, UUID merchantId, UpdateMerchantDto merchantDto);
    void deleteMerchant(UUID workspaceId, UUID merchantId);
    List<Merchant> listAllByWorkspaceId(UUID workspaceId);
}
