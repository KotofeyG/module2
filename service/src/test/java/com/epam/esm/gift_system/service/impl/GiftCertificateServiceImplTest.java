package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.repository.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.gift_system.repository.dao.impl.TagDaoImpl;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import com.epam.esm.gift_system.service.converter.DtoToGiftCertificateConverter;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.converter.GiftCertificateToDtoConverter;
import com.epam.esm.gift_system.service.converter.TagToDtoConverter;
import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import com.epam.esm.gift_system.service.dto.TagDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class GiftCertificateServiceImplTest {
    GiftCertificateServiceImpl service;
    GiftCertificateDaoImpl certificateDao;
    TagDaoImpl tagDao;
    GiftCertificateDto expected;
    GiftCertificate certificate;
    Tag tag;
    TagDto tagDto;

    {
        tag = new Tag(1L, "TestTagName");
        tagDto = new TagDto(1L, "TestTagName");
        certificate = new GiftCertificate(1L, "CertificateName", "Description"
                , new BigDecimal(10.55), 10, LocalDateTime.now(), LocalDateTime.now(), List.of(tag, tag, tag));
        expected = new GiftCertificateDto(1L, "CertificateName", "description"
                , new BigDecimal("10.55"), 10, LocalDateTime.now(), LocalDateTime.now(), List.of(tagDto, tagDto, tagDto));

    }

    @BeforeEach
    void setUp() {
        certificateDao = mock(GiftCertificateDaoImpl.class);
        tagDao = mock(TagDaoImpl.class);
        service = new GiftCertificateServiceImpl(tagDao, certificateDao, new GiftCertificateToDtoConverter(new TagToDtoConverter())
                , new DtoToGiftCertificateConverter(new DtoToTagConverter()), new DtoToTagConverter());
    }

    @Test
    void insertShouldInsertNewCertificate() {
        doReturn(certificate).when(certificateDao).insert(certificate);
        doNothing().when(certificateDao).addTagsToCertificate(certificate.getId(), List.of(tag, tag, tag));
        GiftCertificateDto actual = service.insert(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByAttributes() {
    }

    @Test
    void delete() {
    }
}