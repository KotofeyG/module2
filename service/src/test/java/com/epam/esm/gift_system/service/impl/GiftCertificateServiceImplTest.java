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
import com.epam.esm.gift_system.service.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GiftCertificateServiceImplTest {
    GiftCertificateServiceImpl service;
    GiftCertificateDaoImpl certificateDao;
    TagDaoImpl tagDao;
    GiftCertificateDto expected;
    GiftCertificate certificate;
    Tag tag;
    TagDto tagDto;

    @BeforeEach
    void setUp() {
        tag = new Tag(1L, "TestTagName");
        tagDto = new TagDto(1L, "TestTagName");
        certificate = new GiftCertificate(1L, "CertificateName", "Description"
                , new BigDecimal("10.55"), 10, LocalDateTime.now(), LocalDateTime.now(), List.of(tag, tag, tag));
        expected = new GiftCertificateDto(1L, "CertificateName", "Description"
                , new BigDecimal("10.55"), 10, LocalDateTime.now(), LocalDateTime.now(), List.of(tagDto, tagDto, tagDto));

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
        assertEquals(expected, actual);
    }

    @Test
    void insertShouldThrowExceptionWithNull() {
        try {
            service.insert(null);
            fail(" GiftCertificateServiceImpl.insert(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with null argument");
        } catch (EntityCreationException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithInValidName() {
        expected.setName(null);
        try {
            service.insert(expected);
            fail(" GiftCertificateServiceImpl.insert(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid name argument");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithInValidDescription() {
        expected.setDescription(null);
        try {
            service.insert(expected);
            fail(" GiftCertificateServiceImpl.insert(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid description argument");
        } catch (EntityNotValidDescriptionException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithInValidPrice() {
        expected.setPrice(null);
        try {
            service.insert(expected);
            fail(" GiftCertificateServiceImpl.insert(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid price argument");
        } catch (EntityNotValidPriceException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithInValidDuration() {
        expected.setDuration(0);
        try {
            service.insert(expected);
            fail(" GiftCertificateServiceImpl.insert(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid duration argument");
        } catch (EntityNotValidDurationException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithInValidLastUpdatedDate() {
        expected.setLastUpdateDate(null);
        try {
            service.insert(expected);
            fail(" GiftCertificateServiceImpl.insert(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid last updated date argument");
        } catch (EntityNotValidDateException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateShouldUpdateCurrentCertificate() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(expected.getId());
        doNothing().when(certificateDao).deleteTagsFromCertificate(certificate.getId(), certificate.getTags());
        doNothing().when(certificateDao).addTagsToCertificate(certificate.getId(), certificate.getTags());
        GiftCertificateDto actual = service.update(expected.getId(), expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldThrowExceptionWithInvalidName() {
        expected.setName("<Name>");
        doReturn(Optional.of(certificate)).when(certificateDao).findById(expected.getId());
        try {
            service.update(expected.getId(), expected);
            fail(" GiftCertificateServiceImpl.update(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid name argument");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateShouldThrowExceptionWithInvalidDescription() {
        expected.setDescription("<Description>");
        doReturn(Optional.of(certificate)).when(certificateDao).findById(expected.getId());
        try {
            service.update(expected.getId(), expected);
            fail(" GiftCertificateServiceImpl.update(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid description argument");
        } catch (EntityNotValidDescriptionException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateShouldThrowExceptionWithInvalidPrice() {
        expected.setPrice(new BigDecimal("-1"));
        doReturn(Optional.of(certificate)).when(certificateDao).findById(expected.getId());
        try {
            service.update(expected.getId(), expected);
            fail(" GiftCertificateServiceImpl.update(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid price argument");
        } catch (EntityNotValidPriceException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateShouldThrowExceptionWithInvalidDuration() {
        expected.setDuration(-1);
        doReturn(Optional.of(certificate)).when(certificateDao).findById(expected.getId());
        try {
            service.update(expected.getId(), expected);
            fail(" GiftCertificateServiceImpl.update(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid duration argument");
        } catch (EntityNotValidDurationException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateShouldThrowExceptionWithInvalidLastUpdateTime() {
        expected.setLastUpdateDate(LocalDateTime.now().minusDays(1));
        doReturn(Optional.of(certificate)).when(certificateDao).findById(expected.getId());
        try {
            service.update(expected.getId(), expected);
            fail(" GiftCertificateServiceImpl.update(GiftCertificateDto giftCertificateDto) should throw an exception " +
                    "with invalid last update time argument");
        } catch (EntityNotValidDateException e) {
            assertTrue(true);
        }
    }

    @Test
    void findByIdShouldFindCertificate() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(certificate.getId());
        GiftCertificateDto actual = service.findById(certificate.getId());
        assertEquals(expected, actual);
    }

    @Test
    void findByIdShouldThrowExceptionWithNonExistingCertificate() {
        doReturn(Optional.empty()).when(certificateDao).findById(certificate.getId());
        try {
            service.findById(certificate.getId());
            fail(" GiftCertificateServiceImpl.findById(Long id) should throw an exception with non-existing entity");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void findByAttributesShouldReturnListOfGiftCertificateDto() {
        List<String> fields = List.of("name", "description", "price");
        String orderSort = "desc";
        doReturn(List.of(certificate, certificate, certificate)).when(certificateDao)
                .findByAttributes("", "", fields, orderSort);
        List<GiftCertificateDto> expectedList = service.findByAttributes("", "", fields, orderSort);
        assertEquals(List.of(expected, expected, expected), expectedList);
    }

    @Test
    void findByAttributesShouldThrowExceptionWithInvalidFields() {
        List<String> fields = List.of("", "<description>", "price");
        String orderSort = "desc";
        try {
            service.findByAttributes("", "", fields, orderSort);
            fail(" GiftCertificateServiceImpl.findByAttributes should throw an exception with invalid arguments");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void findByAttributesShouldThrowExceptionWithInvalidOrderSort() {
        List<String> fields = List.of("name", "description", "price");
        String orderSort = "<desc>";
        try {
            service.findByAttributes("", "", fields, orderSort);
            fail(" GiftCertificateServiceImpl.findByAttributes should throw an exception with invalid arguments");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void deleteShouldDeleteCertificateById() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(certificate.getId());
        doReturn(true).when(certificateDao).delete(certificate.getId());
        GiftCertificateDto actual = service.delete(certificate.getId());
        assertEquals(expected, actual);
    }

    @Test
    void deleteShouldThrowExceptionWithNonExistingCertificate() {
        doReturn(Optional.empty()).when(certificateDao).findById(certificate.getId());
        doReturn(false).when(certificateDao).delete(certificate.getId());
        try {
            service.delete(certificate.getId());
            fail(" GiftCertificateServiceImpl.delete(Long id) should throw an exception with non-existing entity");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }
}