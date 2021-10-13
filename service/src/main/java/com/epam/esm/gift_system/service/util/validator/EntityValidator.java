package com.epam.esm.gift_system.service.util.validator;

import com.epam.esm.gift_system.repository.model.EntityField;
import com.epam.esm.gift_system.service.dto.TagDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

@Component
public class EntityValidator {
    private static final int MIN_EXPIRATION_PERIOD = 1;
    private static final int MAX_EXPIRATION_PERIOD = 255;
    private static final Set<String> AVAILABLE_SORT_ORDERS = Set.of("asc", "desc");
    private static final String NAME_REGEX = "[A-Za-zА-Яа-я-, ]{2,75}";
    private static final String DESCRIPTION_REGEX = "[A-Za-zА-Яа-я\\d-.,:;!?()\" ]{2,255}";
    private static final String PRICE_REGEX = "\\d{1,5}|\\d{1,5}\\.\\d{1,2}";

    public enum ValidationType {
        INSERT, UPDATE
    }

    public boolean isNameValid(String name, ValidationType type) {
        return type == ValidationType.INSERT
                ? isNotNullAndBlank(name) && name.matches(NAME_REGEX)
                : name == null || (!name.isBlank() && name.matches(NAME_REGEX));
    }

    public boolean isDescriptionValid(String description, ValidationType type) {
        return type == ValidationType.INSERT
                ? isNotNullAndBlank(description) && description.matches(DESCRIPTION_REGEX)
                : description == null || (!description.isBlank() && description.matches(DESCRIPTION_REGEX));
    }

    private boolean isNotNullAndBlank(String field) {
        return field != null && !field.isBlank();
    }

    public boolean isPriceValid(BigDecimal price, ValidationType type) {
        return type == ValidationType.INSERT
                ? price != null && String.valueOf(price.doubleValue()).matches(PRICE_REGEX)
                : price == null || String.valueOf(price.doubleValue()).matches(PRICE_REGEX);
    }



    public boolean isDurationValid(int duration, ValidationType type) {
        return type == ValidationType.INSERT
                ? duration >= MIN_EXPIRATION_PERIOD & duration <= MAX_EXPIRATION_PERIOD
                : duration == 0 || (duration >= MIN_EXPIRATION_PERIOD & duration <= MAX_EXPIRATION_PERIOD);
    }

    public boolean isTagListValid(List<TagDto> tags, ValidationType type) {
        return type == ValidationType.INSERT
                ? !CollectionUtils.isEmpty(tags) && tags.stream()
                .map(tag -> tag != null && isNameValid(tag.getName(), ValidationType.INSERT))
                .filter(Boolean::booleanValue).count() == tags.size()
                : CollectionUtils.isEmpty(tags) || tags.stream()
                .map(tag -> tag != null && isNameValid(tag.getName(), ValidationType.UPDATE))
                .filter(Boolean::booleanValue).count() == tags.size();
    }

    public boolean isGiftCertificateFieldListValid(List<String> fieldList) {
        return fieldList == null || EntityField.getNameList().containsAll(fieldList);
    }

    public boolean isOrderSortValid(String orderSort) {
        return orderSort == null || AVAILABLE_SORT_ORDERS.contains(orderSort.toLowerCase());
    }
}