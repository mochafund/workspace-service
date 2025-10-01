package com.mochafund.workspaceservice.merchant.service;

import com.mochafund.workspaceservice.common.events.EventEnvelope;
import com.mochafund.workspaceservice.common.events.EventType;
import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import com.mochafund.workspaceservice.kafka.KafkaProducer;
import com.mochafund.workspaceservice.merchant.dto.CreateMerchantDto;
import com.mochafund.workspaceservice.merchant.dto.UpdateMerchantDto;
import com.mochafund.workspaceservice.merchant.entity.Merchant;
import com.mochafund.workspaceservice.merchant.events.MerchantEventPayload;
import com.mochafund.workspaceservice.merchant.repository.IMerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class MerchantService implements IMerchantService {

    private final IMerchantRepository merchantRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional(readOnly = true)
    public List<Merchant> listAllByWorkspaceId(UUID workspaceId) {
        return merchantRepository.findAllByWorkspaceId(workspaceId);
    }

    @Transactional(readOnly = true)
    public Merchant getMerchant(UUID workspaceId, UUID merchantId) {
        return merchantRepository.findByWorkspaceIdAndId(workspaceId, merchantId).orElseThrow(
                () -> new ResourceNotFoundException("Merchant not found"));
    }

    @Transactional
    public Merchant createMerchant(UUID userId, UUID workspaceId, CreateMerchantDto merchantDto) {
        Merchant merchant = CreateMerchantDto.fromDto(merchantDto);
        merchant.setWorkspaceId(workspaceId);
        merchant.setCreatedBy(userId);

        merchant = merchantRepository.save(merchant);

        kafkaProducer.send(EventEnvelope.<MerchantEventPayload>builder()
                .type(EventType.MERCHANT_CREATED)
                .payload(MerchantEventPayload.builder()
                        .id(merchant.getId())
                        .workspaceId(workspaceId)
                        .build())
                .build());

        log.info("Created merchantId={} for name={}", merchant.getId(), merchant.getPayee());
        return merchant;
    }

    @Transactional
    public Merchant updateMerchant(UUID workspaceId, UUID merchantId, UpdateMerchantDto merchantDto) {
        log.info("Updating merchantId={}", merchantId);

        Merchant merchant = this.getMerchant(workspaceId, merchantId);
        merchant.patchFrom(merchantDto);
        merchant = merchantRepository.save(merchant);

        kafkaProducer.send(EventEnvelope.<MerchantEventPayload>builder()
                .type(EventType.MERCHANT_UPDATED)
                .payload(MerchantEventPayload.builder()
                        .id(merchant.getId())
                        .workspaceId(workspaceId)
                        .build())
                .build());

        return merchant;
    }

    @Transactional
    public void deleteMerchant(UUID workspaceId, UUID merchantId) {
       Merchant merchant = this.getMerchant(workspaceId, merchantId);

       log.info("Deleting merchantId={}", merchant.getId());

       merchantRepository.deleteByWorkspaceIdAndId(merchant.getWorkspaceId(), merchant.getId());

        kafkaProducer.send(EventEnvelope.<MerchantEventPayload>builder()
                .type(EventType.MERCHANT_DELETED)
                .payload(MerchantEventPayload.builder()
                        .id(merchant.getId())
                        .workspaceId(workspaceId)
                        .build())
                .build());
    }
}
