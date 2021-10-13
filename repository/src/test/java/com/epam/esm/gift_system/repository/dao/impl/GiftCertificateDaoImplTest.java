package com.epam.esm.gift_system.repository.dao.impl;

import com.epam.esm.gift_system.dbconfig.TestConfig;
import com.epam.esm.gift_system.repository.model.EntityField;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@ActiveProfiles("dev")
class GiftCertificateDaoImplTest {
    private static final String TAG_NAME_FOR_SEARCH= "IT";
    private static final String PART_OF_SEARCH = "descr";

    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";

    private static final long CERTIFICATE_ID = 1;
    private static final String CERTIFICATE_NAME = "Certificate";
    private static final String DESCRIPTION = "Description";
    private static final BigDecimal PRICE = new BigDecimal("100");
    private static final int DURATION = 50;
    private static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    private static final LocalDateTime LAST_UPDATE_DATE = LocalDateTime.now();

    private final GiftCertificateDaoImpl certificateDao;

    private Tag tag;
    private List<Tag> tagList;
    private GiftCertificate certificate;

    @Autowired
    GiftCertificateDaoImplTest(GiftCertificateDaoImpl certificateDao) {
        this.certificateDao = certificateDao;
    }

    @BeforeEach
    void setUp() {
        tag = new Tag(TAG_ID, TAG_NAME);
        tagList = new ArrayList<>(List.of(tag, new Tag(2, "Tag2"), new Tag(3, "Tag3")));
        certificate = new GiftCertificate(CERTIFICATE_ID, CERTIFICATE_NAME, DESCRIPTION, PRICE, DURATION, CREATION_DATE
                , LAST_UPDATE_DATE, tagList);
    }

    @Test
    void create() {
        GiftCertificate actual = certificateDao.create(certificate);
        assertEquals(certificate, actual);
    }

    @Test
    void update() {
        Map<String, Object> updatedFields = new LinkedHashMap<>();
        updatedFields.put(EntityField.NAME.toString(), "rest");
        updatedFields.put(EntityField.PRICE.toString(), new BigDecimal("25.25"));
        boolean condition = certificateDao.update(CERTIFICATE_ID, updatedFields);
        assertTrue(condition);
    }

    @Test
    void updateReturnsFalseWithNonExistingCertificate() {
        Map<String, Object> updatedFields = new LinkedHashMap<>();
        updatedFields.put(EntityField.NAME.toString(), "rest");
        updatedFields.put(EntityField.PRICE.toString(), new BigDecimal("25.25"));
        boolean condition = certificateDao.update(100L, updatedFields);
        assertFalse(condition);
    }

    @Test
    void findById() {
        Optional<GiftCertificate> actual = certificateDao.findById(CERTIFICATE_ID);
        assertTrue(actual.isPresent());
    }

    @Test
    void notFindById() {
        Optional<GiftCertificate> actual = certificateDao.findById(100L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findByAttributesByTagAndPartOFSearch() {
        List<GiftCertificate> actual = certificateDao
                .findByAttributes(TAG_NAME_FOR_SEARCH, PART_OF_SEARCH, null, null);
        assertEquals(actual.size(), 1);
    }

    @Test
    void findByAttributesIfAllParametersNull() {
        List<GiftCertificate> actualList = certificateDao
                .findByAttributes(null, null, null, null);
        assertEquals(actualList.size(), 2);
    }

    @Test
    void deleteReturnsTrueWithExistingCertificate() {
        boolean condition = certificateDao.delete(3L);
        assertTrue(condition);
    }

    @Test
    void deleteReturnsFalseWithNonExistingCertificate() {
        boolean condition = certificateDao.delete(100L);
        assertFalse(condition);
    }

    @Test
    void addTagsToCertificateReturnsTrueWithExistingCertificate() {
        List<Tag> actualList = certificateDao.addTagsToCertificate(3L, tagList);
        assertEquals(tagList, actualList);
    }

    @Test
    void deleteAllTagsFromCertificate() {
        boolean condition = certificateDao.deleteAllTagsFromCertificate(CERTIFICATE_ID);
        assertTrue(condition);
    }
}