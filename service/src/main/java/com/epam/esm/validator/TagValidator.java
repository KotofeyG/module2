package com.epam.esm.validator;

public class TagValidator {

    public static boolean isNameValid(String name) {
        return name != null && !name.isBlank();
    }

    private TagValidator() {
    }
}