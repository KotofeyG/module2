package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.service.converter.DtoToGiftCertificateConverter;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.exception.*;
import com.epam.esm.gift_system.repository.dao.TagDao;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import com.epam.esm.gift_system.service.GiftCertificateService;
import com.epam.esm.gift_system.service.converter.GiftCertificateToDtoConverter;
import com.epam.esm.gift_system.repository.dao.GiftCertificateDao;
import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.gift_system.repository.model.EntityField.*;
import static com.epam.esm.gift_system.service.validator.EntityValidator.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final TagDao tagDao;
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateToDtoConverter GiftCertificateToDtoConverter;
    private final DtoToGiftCertificateConverter toGiftCertificateConverter;
    private final DtoToTagConverter dtoToTagConverter;

    @Autowired
    public GiftCertificateServiceImpl(TagDao tagDao, GiftCertificateDao giftCertificateDao
            , GiftCertificateToDtoConverter giftCertificateToDtoConverter
            , DtoToGiftCertificateConverter toGiftCertificateConverter, DtoToTagConverter dtoToTagConverter) {
        this.tagDao = tagDao;
        this.giftCertificateDao = giftCertificateDao;
        GiftCertificateToDtoConverter = giftCertificateToDtoConverter;
        this.toGiftCertificateConverter = toGiftCertificateConverter;
        this.dtoToTagConverter = dtoToTagConverter;
    }

    @Override
    @Transactional
    public GiftCertificateDto insert(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = toGiftCertificateConverter.convert(giftCertificateDto);
        throwExceptionIfCertificateInvalid(giftCertificate);

        giftCertificate.setTags(giftCertificate.getTags().stream().map(tagDao::findOrCreateTag).toList());
        giftCertificateDao.insert(giftCertificate);
        giftCertificateDao.addTagsToCertificate(giftCertificate.getId(), giftCertificate.getTags());
        return GiftCertificateToDtoConverter.convert(giftCertificate);
    }

    private void throwExceptionIfCertificateInvalid(GiftCertificate giftCertificate) {
        if (!isNameValid(giftCertificate.getName())) {
            throw new EntityNotValidNameException();
        }
        if (!isDescriptionValid(giftCertificate.getDescription())) {
            throw new EntityNotValidDescriptionException();
        }
        if (!isPriceValid(giftCertificate.getPrice())) {
            throw new EntityNotValidPriceException();
        }
        if (!isDurationValid(giftCertificate.getDuration())) {
            throw new EntityNotValidDurationException();
        }
        if (!isLastUpdateDateValid(giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate())) {
            throw new EntityNotValidDateException();
        }
        if (!isTagNameListValid(giftCertificate.getTags())) {
            throw new EntityNotValidTagNameException();
        }
    }

    @Override
    @Transactional
    public GiftCertificateDto update(Long id, GiftCertificateDto updated) {
        GiftCertificateDto selected = giftCertificateDao.findById(id).map(GiftCertificateToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
        Map<String, Object> updatedFields = checkAndGetUpdatedFields(selected, updated);
        List<Tag> updatedTags = updated.getTags().stream().map(dtoToTagConverter::convert).toList();

        if (!updatedTags.isEmpty()) {
            throwExceptionIfTagListInvalid(updatedTags);
            giftCertificateDao.deleteTagsFromCertificate(selected.getId(), selected.getTags().stream().map(dtoToTagConverter::convert).toList());
            giftCertificateDao.addTagsToCertificate(selected.getId(), updatedTags);
            selected.setTags(updated.getTags());
        }
        if (!updatedFields.isEmpty()) {
            giftCertificateDao.update(selected.getId(), updatedFields);
        }
        return selected;
    }

    private Map<String, Object> checkAndGetUpdatedFields(GiftCertificateDto current, GiftCertificateDto updated) {
        String name = updated.getName();
        String description = updated.getDescription();
        BigDecimal price = updated.getPrice();
        int duration = updated.getDuration();
        LocalDateTime lastUpdateDate = updated.getLastUpdateDate();

        Map<String, Object> updatedFields = new LinkedHashMap<>();

        if (name != null && !current.getName().equals(name)) {
            if (!isNameValid(name)) {
                throw new EntityNotValidNameException();
            }
            updatedFields.put(NAME.toString(), name);
            current.setName(name);
        }
        if (description != null && !current.getDescription().equals(description)) {
            if (!isDescriptionValid(description)) {
                throw new EntityNotValidDescriptionException();
            }
            updatedFields.put(DESCRIPTION.toString(), description);
            current.setDescription(description);
        }
        if (price != null && !current.getPrice().equals(price)) {
            if (!isPriceValid(price)) {
                throw new EntityNotValidPriceException();
            }
            updatedFields.put(PRICE.toString(), price);
            current.setPrice(price);
        }
        if (duration != 0 && current.getDuration() != duration) {
            if (!isDurationValid(duration)) {
                throw new EntityNotValidDurationException();
            }
            updatedFields.put(DURATION.toString(), duration);
            current.setDuration(duration);
        }
        if (lastUpdateDate != null && !current.getLastUpdateDate().equals(lastUpdateDate)) {
            if (!isLastUpdateDateValid(current.getCreateDate(), lastUpdateDate)) {
                throw new EntityNotValidDateException();
            }
            updatedFields.put(LAST_UPDATE_DATE.toString(), lastUpdateDate);
            current.setLastUpdateDate(lastUpdateDate);
        }
        return updatedFields;
    }

    private void throwExceptionIfTagListInvalid(List<Tag> newTags) {
        if (newTags != null) {
            if (!isTagNameListValid(newTags)) {
                throw new EntityNotValidNameException();
            }
        }
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        return giftCertificateDao.findById(id).map(GiftCertificateToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<GiftCertificateDto> findByAttributes(String tagName, String searchPart, List<String> sortingFields, String orderSort) {
        if (isGiftCertificateFieldListValid(sortingFields) && isOrderSortValid(orderSort)) {
            List<GiftCertificate> giftCertificateList = giftCertificateDao.findByAttributes(tagName, searchPart, sortingFields, orderSort);
            return giftCertificateList.stream().map(GiftCertificateToDtoConverter::convert).toList();
        }
        throw new EntityNotValidNameException();
    }

    @Override
    public GiftCertificateDto delete(Long id) {
        GiftCertificateDto deletedGiftCertificate = giftCertificateDao.findById(id).map(GiftCertificateToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
        giftCertificateDao.delete(id);
        return deletedGiftCertificate;
    }
}