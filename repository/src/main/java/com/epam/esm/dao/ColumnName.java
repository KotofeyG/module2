package com.epam.esm.dao;

public class ColumnName {
    /* tags table */
    public static String TAG_ID = "tags.id";
    public static String TAG_NAME = "tags.name";

    /* gift_certificates table */
    public static String CERTIFICATE_ID = "gift_certificates.id";
    public static String CERTIFICATE_NAME = "gift_certificates.name";
    public static String CERTIFICATE_DESCRIPTION = "gift_certificates.description";
    public static String CERTIFICATE_PRICE = "gift_certificates.price";
    public static String CERTIFICATE_DURATION = "gift_certificates.duration";
    public static String CERTIFICATE_CREATE_DATE = "gift_certificates.create_date";
    public static String CERTIFICATE_LUST_UPDATE_DATE = "gift_certificates.last_update_date";

    /* tags_certificates table */
    public static String TAGS_CERTIFICATES_TAG_ID = "tags_certificates.tag_id";
    public static String TAGS_CERTIFICATES_CERTIFICATE_ID = "tags_certificates.gift_certificate_id";

    private ColumnName() {
    }
}