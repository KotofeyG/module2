package com.epam.esm.gift_system.service.validator;

import com.epam.esm.gift_system.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.gift_system.service.validator.EntityValidator.ValidationType.*;
import static org.junit.jupiter.api.Assertions.*;

class EntityValidatorTest {
    private EntityValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EntityValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Name", "Tag-name", "another-tag-name", "Ra"})
    void isNameValidReturnsTrueWithInsertedValidParams(String validName) {
        boolean condition = validator.isNameValid(validName, INSERT);
        assertTrue(condition);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"<Name>", "N", "Name!", "Name&", "'Name'", "Name?"})
    void isNameValidReturnsFalseWithInsertedInvalidParams(String invalidName) {
        boolean condition = validator.isNameValid(invalidName, INSERT);
        assertFalse(condition);
    }

    @Test
    void isNameValidReturnsFalseWithInsertedMoreThen75SymbolsName() {
        String MoreThen75SymbolsName = "N".repeat(76);
        boolean condition = validator.isNameValid(MoreThen75SymbolsName, INSERT);
        assertFalse(condition);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Name", "Tag-name", "another-tag-name", "Ra"})
    void isNameValidReturnsTrueWithUpdatedValidParams(String validName) {
        boolean condition = validator.isNameValid(validName, UPDATE);
        assertTrue(condition);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"<Name>", "N", "Name!", "Name&", "'Name'", "Name?"})
    void isNameValidReturnsFalseWithUpdatedInvalidParams(String invalidName) {
        boolean condition = validator.isNameValid(invalidName, UPDATE);
        assertFalse(condition);
    }

    @Test
    void isNameValidReturnsFalseWithUpdatedMoreThen75SymbolsName() {
        String MoreThen75SymbolsName = "N".repeat(76);
        boolean condition = validator.isNameValid(MoreThen75SymbolsName, UPDATE);
        assertFalse(condition);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Description", "Description!", "Description?", "Description;", "Description.", "-Description-"})
    void isDescriptionValidReturnsTrueWithInsertedValidDescription(String validDescription) {
        boolean condition = validator.isDescriptionValid(validDescription, INSERT);
        assertTrue(condition);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"<Description>", "Description%", "Description^", "Description&", "Description~", "Description@", "D"})
    void isDescriptionValidReturnsFalseWithInsertedInvalidDescription(String invalidDescription) {
        boolean condition = validator.isDescriptionValid(invalidDescription, INSERT);
        assertFalse(condition);
    }

    @Test
    void isDescriptionValidReturnsFalseWithInsertedMoreThen255SymbolsDescription() {
        String MoreThen255Description = "D".repeat(256);
        boolean condition = validator.isDescriptionValid(MoreThen255Description, INSERT);
        assertFalse(condition);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"Description", "Description!", "Description?", "Description;", "Description.", "-Description-"})
    void isDescriptionValidReturnsTrueWithUpdatedValidDescription(String validDescription) {
        boolean condition = validator.isDescriptionValid(validDescription, UPDATE);
        assertTrue(condition);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"<Description>", "Description%", "Description^", "Description&", "Description~", "Description@", "D"})
    void isDescriptionValidReturnsFalseWithUpdatedInvalidDescription(String invalidDescription) {
        boolean condition = validator.isDescriptionValid(invalidDescription, UPDATE);
        assertFalse(condition);
    }

    @Test
    void isDescriptionValidReturnsFalseWithUpdatedMoreThen255SymbolsDescription() {
        String MoreThen255Description = "D".repeat(256);
        boolean condition = validator.isDescriptionValid(MoreThen255Description, UPDATE);
        assertFalse(condition);
    }

    @ParameterizedTest
    @ValueSource(strings = {"25.25", "5", "0", "100", "10000", "0.2"})
    void isPriceValidReturnsTrueWithInsertedValidPrice(String strPrice) {
        BigDecimal validPrice = new BigDecimal(strPrice);
        boolean condition = validator.isPriceValid(validPrice, INSERT);
        assertTrue(condition);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-20", "555555.55", "55.555"})
    void isPriceValidReturnsFalseWithInsertedValidPrice(String strPrice) {
        BigDecimal validPrice = strPrice != null ? new BigDecimal(strPrice) : null;
        boolean condition = validator.isPriceValid(validPrice, INSERT);
        assertFalse(condition);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"25.25", "5", "0", "100", "10000", "0.2"})
    void isPriceValidReturnsTrueWithUpdatedValidPrice(String strPrice) {
        BigDecimal validPrice = strPrice != null ? new BigDecimal(strPrice) : null;
        boolean condition = validator.isPriceValid(validPrice, UPDATE);
        assertTrue(condition);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-20", "555555.55", "55.555"})
    void isPriceValidReturnsFalseWithUpdatedInvalidPrice(String strPrice) {
        BigDecimal invalidPrice = new BigDecimal(strPrice);
        boolean condition = validator.isPriceValid(invalidPrice, UPDATE);
        assertFalse(condition);
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 10, 200, 255})
    void isDurationValidReturnsTrueWithInsertedValidDuration(int validDuration) {
        boolean condition = validator.isDurationValid(validDuration, INSERT);
        assertTrue(condition);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -10, 256})
    void isDurationValidReturnsFalseWithInsertedInvalidDuration(int invalidDuration) {
        boolean condition = validator.isDurationValid(invalidDuration, INSERT);
        assertFalse(condition);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 10, 200, 255})
    void isDurationValidReturnsTrueWithUpdatedValidDuration(int validDuration) {
        boolean condition = validator.isDurationValid(validDuration, UPDATE);
        assertTrue(condition);
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, 256})
    void isDurationValidReturnsFalseWithUpdatedInvalidDuration(int invalidDuration) {
        boolean condition = validator.isDurationValid(invalidDuration, UPDATE);
        assertFalse(condition);
    }

    @Test
    void isInsertedTagListValid() {
        List<TagDto> validTagList = List.of(new TagDto(0, "NameOne"), new TagDto(0, "NameTwo")
                , new TagDto(0, "NameThree"));
        boolean condition = validator.isTagListValid(validTagList, INSERT);
        assertTrue(condition);
    }

    @Test
    void isTagNameListValidReturnsFalseWithInsertedEmptyTagList() {
        List<TagDto> emptyTagList = List.of();
        boolean condition = validator.isTagListValid(emptyTagList, INSERT);
        assertFalse(condition);
    }

    @Test
    void isTagNameListValidReturnsFalseWithInsertedNullTagList() {
        List<TagDto> nullTagList = null;
        boolean condition = validator.isTagListValid(nullTagList, INSERT);
        assertFalse(condition);
    }

    @Test
    void isTagNameListValidReturnsFalseWithInsertedNullTagInList() {
        List<TagDto> tagListWithNullTag = new ArrayList<>();
        tagListWithNullTag.add(new TagDto(0, "NameOne"));
        tagListWithNullTag.add(null);
        boolean condition = validator.isTagListValid(tagListWithNullTag, INSERT);
        assertFalse(condition);
    }

    @Test
    void isTagNameListValidReturnsFalseWithInsertedInvalidTagInList() {
        List<TagDto> tagListWithInvalidTag = List.of(new TagDto(0, "NameOne")
                , new TagDto(0, "<NameTwo>"));
        boolean condition = validator.isTagListValid(tagListWithInvalidTag, INSERT);
        assertFalse(condition);
    }

    @Test
    void isUpdatedTagListValid() {
        List<TagDto> validTagList = List.of(new TagDto(0, "NameOne"), new TagDto(0, "NameTwo")
                , new TagDto(0, "NameThree"));
        boolean condition = validator.isTagListValid(validTagList, UPDATE);
        assertTrue(condition);
    }

    @Test
    void isTagNameListValidReturnsTrueWithUpdatedEmptyTagList() {
        List<TagDto> emptyTagList = List.of();
        boolean condition = validator.isTagListValid(emptyTagList, UPDATE);
        assertTrue(condition);
    }

    @Test
    void isTagNameListValidReturnsTrueWithUpdatedNullTagList() {
        List<TagDto> nullTagList = null;
        boolean condition = validator.isTagListValid(nullTagList, UPDATE);
        assertTrue(condition);
    }

    @Test
    void isTagNameListValidReturnsFalseWithUpdatedNullTagInList() {
        List<TagDto> tagListWithNullTag = new ArrayList<>();
        tagListWithNullTag.add(new TagDto(0, "NameOne"));
        tagListWithNullTag.add(null);
        boolean condition = validator.isTagListValid(tagListWithNullTag, UPDATE);
        assertFalse(condition);
    }

    @Test
    void isTagNameListValidReturnsFalseWithUpdatedInvalidTagInList() {
        List<TagDto> tagListWithInvalidTag = List.of(new TagDto(0, "NameOne")
                , new TagDto(0, "<NameTwo>"));
        boolean condition = validator.isTagListValid(tagListWithInvalidTag, UPDATE);
        assertFalse(condition);
    }

    @Test
    void isGiftCertificateFieldListValidReturnsTrueWithValidParams() {
        List<String> fieldList = List.of("name", "description", "price", "duration", "createDate", "lastUpdateDate");
        boolean condition = validator.isGiftCertificateFieldListValid(fieldList);
        assertTrue(condition);
    }

    @ParameterizedTest
    @ValueSource(strings = {"<createDate>", "create", "createDate!", "?createDate"})
    void isGiftCertificateFieldListValidReturnsFalseWithValidParams(String invalidField) {
        List<String> fieldList = List.of("name", "description", "price", "duration", invalidField, "lastUpdateDate");
        boolean condition = validator.isGiftCertificateFieldListValid(fieldList);
        assertFalse(condition);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"asc", "desc", "ASC", "DESC", "desC", "AsC"})
    void isOrderSortValidReturnsTrue(String orderSort) {
        boolean condition = validator.isOrderSortValid(orderSort);
        assertTrue(condition);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "down", "up", "desC ", " Asc"})
    void isOrderSortValidReturnsFalseWithInvalidArg(String orderSort) {
        boolean condition = validator.isOrderSortValid(orderSort);
        assertFalse(condition);
    }
}