package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.repository.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.gift_system.repository.model.EntityField;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import com.epam.esm.gift_system.service.converter.DtoToGiftCertificateConverter;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.converter.GiftCertificateToDtoConverter;
import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import com.epam.esm.gift_system.service.dto.TagDto;
import com.epam.esm.gift_system.service.exception.*;
import com.epam.esm.gift_system.service.util.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.gift_system.service.util.validator.EntityValidator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";

    private static final long CERTIFICATE_ID = 1;
    private static final String CERTIFICATE_NAME = "Certificate";
    private static final String DESCRIPTION = "Description";
    private static final BigDecimal PRICE = new BigDecimal("100");
    private static final int DURATION = 50;
    private static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    private static final LocalDateTime LAST_UPDATE_DATE = LocalDateTime.now();

    private static final String ORDER_SORT = "desc";

    private Tag tag;
    private TagDto tagDto;
    private List<Tag> tagList;
    private List<TagDto> tagDtoList;
    private GiftCertificate certificate;
    private GiftCertificateDto expected;
    private List<String> sortingFieldList;
    private List<GiftCertificateDto> expectedList;

    @InjectMocks
    private GiftCertificateServiceImpl service;
    @Mock
    private GiftCertificateDaoImpl certificateDao;
    @Mock
    private EntityValidator validator;
    @Mock
    private DtoToGiftCertificateConverter toCertificateConverter;
    @Mock
    private GiftCertificateToDtoConverter toCertificateDtoConverter;
    @Mock
    private DtoToTagConverter toTagConverter;

    @BeforeEach
    void setUp() {
        tag = new Tag(TAG_ID, TAG_NAME);
        tagDto = new TagDto(TAG_ID, TAG_NAME);
        tagList = new ArrayList<>(List.of(tag, tag, tag));
        tagDtoList = new ArrayList<>(List.of(tagDto, tagDto, tagDto));
        certificate = new GiftCertificate(CERTIFICATE_ID, CERTIFICATE_NAME, DESCRIPTION, PRICE, DURATION, CREATION_DATE
                , LAST_UPDATE_DATE, tagList);
        expected = new GiftCertificateDto(CERTIFICATE_ID, CERTIFICATE_NAME, DESCRIPTION, PRICE, DURATION, CREATION_DATE
                , LAST_UPDATE_DATE, tagDtoList);
        sortingFieldList = List.of(EntityField.NAME.toString(), EntityField.PRICE.toString());
        expectedList = List.of(expected, expected, expected);
    }

    @Test
    void create() {
        setCheckFieldsResult();
        doReturn(certificate).when(toCertificateConverter).convert(Mockito.any(GiftCertificateDto.class));
        doReturn(certificate).when(certificateDao).create(Mockito.any(GiftCertificate.class));
        doReturn(tagList).when(certificateDao).addTagsToCertificate(Mockito.anyLong(), Mockito.anyList());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void createThrowExceptionWhenArgNull() {
        try {
            service.create(null);
            fail("Method insert() should throw exception EntityCreationException");
        } catch (EntityCreationException e) {
            assertTrue(true);
        }
    }

    @Test
    void createThrowExceptionWhenNameInvalid() {
        doReturn(false).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityNotValidNameException");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void createThrowExceptionWhenDescriptionInvalid() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityNotValidDescriptionException");
        } catch (EntityNotValidDescriptionException e) {
            assertTrue(true);
        }
    }

    @Test
    void createThrowExceptionWhenPriceInvalid() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityNotValidPriceException");
        } catch (EntityNotValidPriceException e) {
            assertTrue(true);
        }
    }

    @Test
    void createThrowExceptionWhenDurationInvalid() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isDurationValid(Mockito.anyInt(), Mockito.any(ValidationType.class));
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityNotValidDurationException");
        } catch (EntityNotValidDurationException e) {
            assertTrue(true);
        }
    }

    @Test
    void createThrowExceptionWhenTagNameListInvalid() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDurationValid(Mockito.anyInt(), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isTagListValid(Mockito.anyList(), Mockito.any(ValidationType.class));
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityNotValidTagNameException");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void update() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        setCheckFieldsResult();
        doNothing().when(certificateDao).update(Mockito.anyLong(), Mockito.anyMap());
        doReturn(tag).when(toTagConverter).convert(Mockito.any(TagDto.class));
        doNothing().when(certificateDao).deleteAllTagsFromCertificate(Mockito.anyLong());
        doReturn(tagList).when(certificateDao).addTagsToCertificate(Mockito.anyLong(), Mockito.anyList());
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.update(expected.getId(), expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateWithEmptyTagList() {
        expected.setTags(List.of());
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        setCheckFieldsResult();
        doNothing().when(certificateDao).update(Mockito.anyLong(), Mockito.anyMap());
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.update(expected.getId(), expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateWithNullTagList() {
        expected.setTags(null);
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDurationValid(Mockito.anyInt(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isTagListValid(expected.getTags(), ValidationType.UPDATE);
        doNothing().when(certificateDao).update(Mockito.anyLong(), Mockito.anyMap());
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.update(expected.getId(), expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateWithoutFields() {
        expected = new GiftCertificateDto();
        expected.setTags(tagDtoList);
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(true).when(validator).isNameValid(expected.getName(), ValidationType.UPDATE);
        doReturn(true).when(validator).isDescriptionValid(expected.getDescription(), ValidationType.UPDATE);
        doReturn(true).when(validator).isPriceValid(expected.getPrice(), ValidationType.UPDATE);
        doReturn(true).when(validator).isDurationValid(expected.getDuration(), ValidationType.UPDATE);
        doReturn(true).when(validator).isTagListValid(Mockito.anyList(), Mockito.any(ValidationType.class));
        doReturn(tag).when(toTagConverter).convert(Mockito.any(TagDto.class));
        doNothing().when(certificateDao).deleteAllTagsFromCertificate(Mockito.anyLong());
        doReturn(tagList).when(certificateDao).addTagsToCertificate(Mockito.anyLong(), Mockito.anyList());
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.update(expected.getId(), expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateThrowExceptionWhenArgNull() {
        try {
            service.update(CERTIFICATE_ID, null);
            fail("Method insert should throw exception EntityCreationException");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionWhenResourceDoesntExist() {
        try {
            doReturn(false).when(certificateDao).isExisting(Mockito.anyLong());
            service.update(CERTIFICATE_ID, expected);
            fail("Method insert should throw exception EntityCreationException");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionWhenNameInvalid() {
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(false).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        try {
            service.update(CERTIFICATE_ID, expected);
            fail("Method insert should throw exception EntityNotValidNameException");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionWhenDescriptionInvalid() {
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        try {
            service.update(CERTIFICATE_ID, expected);
            fail("Method insert should throw exception EntityNotValidDescriptionException");
        } catch (EntityNotValidDescriptionException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionWhenPriceInvalid() {
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        try {
            service.update(CERTIFICATE_ID, expected);
            fail("Method insert should throw exception EntityNotValidPriceException");
        } catch (EntityNotValidPriceException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionWhenDurationInvalid() {
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isDurationValid(Mockito.anyInt(), Mockito.any(ValidationType.class));
        try {
            service.update(CERTIFICATE_ID, expected);
            fail("Method insert should throw exception EntityNotValidDurationException");
        } catch (EntityNotValidDurationException e) {
            assertTrue(true);
        }
    }

    @Test
    void updateThrowExceptionWhenTagNameListInvalid() {
        doReturn(true).when(certificateDao).isExisting(Mockito.anyLong());
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDurationValid(Mockito.anyInt(), Mockito.any(ValidationType.class));
        doReturn(false).when(validator).isTagListValid(Mockito.anyList(), Mockito.any(ValidationType.class));
        try {
            service.update(CERTIFICATE_ID, expected);
            fail("Method insert should throw exception EntityNotValidTagNameException");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void findById() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        GiftCertificateDto actual = service.findById(CERTIFICATE_ID);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdThrowExceptionWhenResourceDoesntExist() {
        doReturn(Optional.empty()).when(certificateDao).findById(Mockito.anyLong());
        try {
            service.findById(CERTIFICATE_ID);
            fail("Method findById should throw exception EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void findByAttributes() {
        doReturn(true).when(validator).isGiftCertificateFieldListValid(Mockito.anyList());
        doReturn(true).when(validator).isOrderSortValid(Mockito.anyString());
        doReturn(List.of(certificate, certificate, certificate)).when(certificateDao).findByAttributes
                (Mockito.anyString(), Mockito.anyString(), Mockito.anyList(), Mockito.anyString());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        List<GiftCertificateDto> actualList = service.findByAttributes(TAG_NAME, DESCRIPTION, sortingFieldList, ORDER_SORT);
        assertEquals(expectedList, actualList);
    }

    @Test
    void findByAttributesReturnsEmptyList() {
        doReturn(true).when(validator).isGiftCertificateFieldListValid(Mockito.anyList());
        doReturn(true).when(validator).isOrderSortValid(Mockito.anyString());
        doReturn(List.of()).when(certificateDao).findByAttributes(Mockito.anyString(), Mockito.anyString()
                , Mockito.anyList(), Mockito.anyString());
        List<GiftCertificateDto> actualList = service.findByAttributes(TAG_NAME, DESCRIPTION, sortingFieldList, ORDER_SORT);
        assertEquals(List.of(), actualList);
    }

    @Test
    void findByAttributesThrowExceptionWhenFieldsInvalid() {
        doReturn(false).when(validator).isGiftCertificateFieldListValid(Mockito.anyList());
        try {
            service.findByAttributes(TAG_NAME, DESCRIPTION, sortingFieldList, ORDER_SORT);
            fail("Method findByAttributes should throw exception EntityNotValidNameException");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void findByAttributesThrowExceptionWhenOrderSortInvalid() {
        doReturn(true).when(validator).isGiftCertificateFieldListValid(Mockito.anyList());
        doReturn(false).when(validator).isOrderSortValid(Mockito.anyString());
        try {
            service.findByAttributes(TAG_NAME, DESCRIPTION, sortingFieldList, ORDER_SORT);
            fail("Method findByAttributes should throw exception EntityNotValidNameException");
        } catch (EntityNotValidNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void delete() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        doReturn(true).when(certificateDao).delete(CERTIFICATE_ID);
        GiftCertificateDto actual = service.delete(CERTIFICATE_ID);
        assertEquals(expected, actual);
    }

    @Test
    void deleteThrowExceptionWhenResourceDoesntExist() {
        doReturn(Optional.empty()).when(certificateDao).findById(Mockito.anyLong());
        try {
            service.delete(CERTIFICATE_ID);
            fail("Method delete should throw exception EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void deleteThrowExceptionWhenResourceCannotBeDeleted() {
        doReturn(Optional.of(certificate)).when(certificateDao).findById(Mockito.anyLong());
        doReturn(expected).when(toCertificateDtoConverter).convert(Mockito.any(GiftCertificate.class));
        doReturn(false).when(certificateDao).delete(CERTIFICATE_ID);
        try {
            service.delete(CERTIFICATE_ID);
            fail("Method delete should throw exception InternalServerException");
        } catch (InternalServerException e) {
            assertTrue(true);
        }
    }

    private void setCheckFieldsResult() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDescriptionValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isPriceValid(Mockito.any(BigDecimal.class), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isDurationValid(Mockito.anyInt(), Mockito.any(ValidationType.class));
        doReturn(true).when(validator).isTagListValid(Mockito.anyList(), Mockito.any(ValidationType.class));
    }
}