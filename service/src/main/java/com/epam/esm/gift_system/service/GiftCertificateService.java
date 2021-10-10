package com.epam.esm.gift_system.service;

import com.epam.esm.gift_system.service.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto insert(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto update(Long id, GiftCertificateDto giftCertificateDto);

    GiftCertificateDto findById(Long id);

    List<GiftCertificateDto> findByAttributes(String tagName, String searchPart, List<String> sortingFields, String orderSort);

    GiftCertificateDto delete(Long id);
}