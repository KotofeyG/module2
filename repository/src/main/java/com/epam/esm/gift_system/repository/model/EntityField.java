package com.epam.esm.gift_system.repository.model;

public enum EntityField {
    ID, NAME, DESCRIPTION, PRICE, DURATION, LAST_UPDATE_DATE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}