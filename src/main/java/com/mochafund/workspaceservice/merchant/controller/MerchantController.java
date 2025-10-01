package com.mochafund.workspaceservice.merchant.controller;

import com.mochafund.workspaceservice.common.annotations.UserId;
import com.mochafund.workspaceservice.common.annotations.WorkspaceId;
import com.mochafund.workspaceservice.merchant.dto.CreateMerchantDto;
import com.mochafund.workspaceservice.merchant.dto.MerchantDto;
import com.mochafund.workspaceservice.merchant.dto.UpdateMerchantDto;
import com.mochafund.workspaceservice.merchant.entity.Merchant;
import com.mochafund.workspaceservice.merchant.service.IMerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchants")
public class MerchantController {

    private final IMerchantService merchantService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MerchantDto>> getAllMerchants(@WorkspaceId UUID workspaceId) {
        List<Merchant> merchants = merchantService.listAllByWorkspaceId(workspaceId);
        return ResponseEntity.ok().body(MerchantDto.fromEntities(merchants));
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(value = "/{merchantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MerchantDto> getMerchant(@WorkspaceId UUID workspaceId, @PathVariable UUID merchantId) {
        Merchant merchant = merchantService.getMerchant(workspaceId, merchantId);
        return ResponseEntity.ok().body(MerchantDto.fromEntity(merchant));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MerchantDto> createMerchant(
            @UserId UUID userId, @WorkspaceId UUID workspaceId,
            @Valid @RequestBody CreateMerchantDto merchantDto
    ) {
        Merchant merchant = merchantService.createMerchant(userId, workspaceId, merchantDto);
        return ResponseEntity.status(201).body(MerchantDto.fromEntity(merchant));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PatchMapping(value = "/{merchantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MerchantDto> updateMerchant(
            @WorkspaceId UUID workspaceId, @PathVariable UUID merchantId,
            @Valid @RequestBody UpdateMerchantDto merchantDto) {
        Merchant merchant = merchantService.updateMerchant(workspaceId, merchantId, merchantDto);
        return ResponseEntity.ok().body(MerchantDto.fromEntity(merchant));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @DeleteMapping(value = "/{merchantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMerchant(@WorkspaceId UUID workspaceId, @PathVariable UUID merchantId) {
        merchantService.deleteMerchant(workspaceId, merchantId);
        return ResponseEntity.noContent().build();
    }
}
