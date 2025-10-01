package com.mochafund.workspaceservice.merchant.repository;

import com.mochafund.workspaceservice.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMerchantRepository extends JpaRepository<Merchant, UUID> {
    Optional<Merchant> findByWorkspaceIdAndId(UUID workspaceId, UUID merchantId);
    List<Merchant> findAllByWorkspaceId(UUID workspaceId);
    void deleteByWorkspaceIdAndId(UUID workspaceId, UUID merchantId);
}
