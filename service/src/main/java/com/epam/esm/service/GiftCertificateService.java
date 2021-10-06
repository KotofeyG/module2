package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto insert(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto findById(long id);

    GiftCertificateDto findByName(String name);

    List<GiftCertificateDto> findAll();

    boolean delete(long id);
}