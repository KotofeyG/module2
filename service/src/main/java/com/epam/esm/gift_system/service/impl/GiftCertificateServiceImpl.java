package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.service.converter.DtoToGiftCertificateConverter;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.dto.TagDto;
import com.epam.esm.gift_system.service.exception.*;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import com.epam.esm.gift_system.service.GiftCertificateService;
import com.epam.esm.gift_system.service.converter.GiftCertificateToDtoConverter;
import com.epam.esm.gift_system.repository.dao.GiftCertificateDao;
import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import com.epam.esm.gift_system.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.gift_system.repository.model.EntityField.*;
import static com.epam.esm.gift_system.repository.model.EntityField.DURATION;
import static com.epam.esm.gift_system.service.validator.EntityValidator.ValidationType.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final EntityValidator validator;
    private final GiftCertificateDao certificateDao;
    private final DtoToTagConverter toTagConverter;
    private final DtoToGiftCertificateConverter toCertificateConverter;
    private final GiftCertificateToDtoConverter toCertificateDtoConverter;

    @Autowired
    public GiftCertificateServiceImpl(EntityValidator validator, GiftCertificateDao certificateDao
            , DtoToTagConverter toTagConverter, DtoToGiftCertificateConverter toCertificateConverter
            , GiftCertificateToDtoConverter toCertificateDtoConverter) {
        this.validator = validator;
        this.certificateDao = certificateDao;
        this.toTagConverter = toTagConverter;
        this.toCertificateConverter = toCertificateConverter;
        this.toCertificateDtoConverter = toCertificateDtoConverter;
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto certificateDto) {
        if (certificateDto == null) {
            throw new EntityCreationException();
        }
        checkCertificateValidation(certificateDto, INSERT);
        GiftCertificate certificate = toCertificateConverter.convert(certificateDto);
        certificateDao.create(certificate);
        List<Tag> addedTags = certificateDao.addTagsToCertificate(certificate.getId(), certificate.getTags());
        certificate.setTags(addedTags);
        return toCertificateDtoConverter.convert(certificate);
    }

    @Override
    @Transactional
    public GiftCertificateDto update(Long id, GiftCertificateDto updatedDto) {
        GiftCertificateDto currentDto = findById(id);
        if (updatedDto == null) {
            throw new EntityNotFoundException();
        }
        checkCertificateValidation(updatedDto, UPDATE);
        Map<String, Object> updatedFieldMap = getUpdatedFieldMap(currentDto, updatedDto);
        if (!updatedFieldMap.isEmpty()) {
            certificateDao.update(id, updatedFieldMap);
        }
        updateTagListInCertificate(id, updatedDto.getTags());
        return findById(id);
    }

    private void updateTagListInCertificate(Long id, List<TagDto> tags) {
        if (!CollectionUtils.isEmpty(tags)) {
            List<Tag> updatedTagList = tags.stream().map(toTagConverter::convert).toList();
            certificateDao.deleteAllTagsFromCertificate(id);
            certificateDao.addTagsToCertificate(id, updatedTagList);
        }
    }

    private Map<String, Object> getUpdatedFieldMap(GiftCertificateDto currentDto, GiftCertificateDto updatedDto) {
        Map<String, Object> fieldList = new LinkedHashMap<>();
        String name = updatedDto.getName();
        String description = updatedDto.getDescription();
        BigDecimal price = updatedDto.getPrice();
        int duration = updatedDto.getDuration();

        if (name != null && !currentDto.getName().equals(name)) {
            fieldList.put(NAME.toString(), name);
        }
        if (description != null && !currentDto.getDescription().equals(description)) {
            fieldList.put(DESCRIPTION.toString(), description);
        }
        if (price != null && !currentDto.getPrice().equals(price)) {
            fieldList.put(PRICE.toString(), price);
        }
        if (duration != 0 && currentDto.getDuration() != duration) {
            fieldList.put(DURATION.toString(), duration);
        }
        return fieldList;
    }

    private void checkCertificateValidation(GiftCertificateDto certificateDto, EntityValidator.ValidationType type) {
        if (!validator.isNameValid(certificateDto.getName(), type)) {
            throw new EntityNotValidNameException();
        }
        if (!validator.isDescriptionValid(certificateDto.getDescription(), type)) {
            throw new EntityNotValidDescriptionException();
        }
        if (!validator.isPriceValid(certificateDto.getPrice(), type)) {
            throw new EntityNotValidPriceException();
        }
        if (!validator.isDurationValid(certificateDto.getDuration(), type)) {
            throw new EntityNotValidDurationException();
        }
        if (!validator.isTagListValid(certificateDto.getTags(), type)) {
            throw new EntityNotValidTagNameException();
        }
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        return certificateDao.findById(id).map(toCertificateDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<GiftCertificateDto> findByAttributes(String tagName, String searchPart, List<String> sortingFieldList, String orderSort) {
        if (validator.isGiftCertificateFieldListValid(sortingFieldList) && validator.isOrderSortValid(orderSort)) {
            List<GiftCertificate> giftCertificateList = certificateDao.findByAttributes(tagName, searchPart, sortingFieldList, orderSort);
            return giftCertificateList.stream().map(toCertificateDtoConverter::convert).toList();
        }
        throw new EntityNotValidNameException();
    }

    @Override
    public GiftCertificateDto delete(Long id) {
        GiftCertificateDto deletedGiftCertificate = findById(id);
        if (!certificateDao.delete(id)) {
            throw new InternalServerException();
        }
        return deletedGiftCertificate;
    }
}