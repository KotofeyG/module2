package com.epam.esm.gift_system.service.validator;

import com.epam.esm.gift_system.repository.model.EntityField;
import com.epam.esm.gift_system.repository.model.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class EntityValidator {
    private static final int MIN_EXPIRATION_PERIOD = 1;
    private static final int MAX_EXPIRATION_PERIOD = 255;
    private static final Set<String> AVAILABLE_SORT_ORDERS = Set.of("asc", "desc", "ASC", "DESC", "");
    private static final String NAME_REGEX = "[A-Za-zА-Яа-я-, ]{2,75}";
    private static final String DESCRIPTION_REGEX = "[A-Za-zА-Яа-я\\d-.,:;!?()\" ]{2,255}";
    private static final String PRICE_REGEX = "[\\d^0]{1,5}|\\d{1,5}\\.\\d{1,2}";

    public static boolean isNameValid(String name) {
        return name != null && !name.isBlank() && name.matches(NAME_REGEX);
    }

    public static boolean isDescriptionValid(String description) {
        return description != null && !description.isBlank() && description.matches(DESCRIPTION_REGEX);
    }

    public static boolean isPriceValid(BigDecimal price) {
        return price != null && String.valueOf(price.doubleValue()).matches(PRICE_REGEX);
    }

    public static boolean isDurationValid(int duration) {
        return duration >= MIN_EXPIRATION_PERIOD & duration <= MAX_EXPIRATION_PERIOD;
    }

    public static boolean isLastUpdateDateValid(LocalDateTime createDate, LocalDateTime lastUpdateDate) {
        return createDate != null && lastUpdateDate != null && lastUpdateDate.compareTo(createDate) >= 0;
    }

    public static boolean isTagNameListValid(List<Tag> tags) {
        return tags != null && !tags.isEmpty() && tags.stream()
                .map(tag -> tag != null && isNameValid(tag.getName()))
                .filter(Boolean::booleanValue).count() == tags.size();
    }

    public static boolean isGiftCertificateFieldListValid(List<String> fieldList) {
        boolean result = fieldList != null;
        if (result) {
            List<String> gitCertificateFieldList = Arrays.stream(EntityField.values()).map(EntityField::toString).toList();
            result = gitCertificateFieldList.containsAll(fieldList);
        }
        return result;
    }

    public static boolean isOrderSortValid(String orderSort) {
        return orderSort == null || AVAILABLE_SORT_ORDERS.contains(orderSort);
    }

    private EntityValidator() {
    }
}