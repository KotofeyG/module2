package com.epam.esm.gift_system.repository.dao.constant;

public class GeneralConstant {
    /* parameter indexes for PrepareStatement */
    public static int FIRST_PARAM_INDEX = 1;
    public static int SECOND_PARAM_INDEX = 2;
    public static int THIRD_PARAM_INDEX = 3;
    public static int FOURTH_PARAM_INDEX = 4;
    public static int FIFTH_PARAM_INDEX = 5;
    public static int SIXTH_PARAM_INDEX = 6;

    /* tags table */
    public static String TAG_ID = "tags.id";
    public static String TAG_NAME = "tags.name";

    /* gift_certificates table */
    public static String CERTIFICATE = "gift_certificates.";
    public static String CERTIFICATE_ID = CERTIFICATE + "id";
    public static String CERTIFICATE_NAME = CERTIFICATE + "name";
    public static String CERTIFICATE_DESCRIPTION = CERTIFICATE + "description";
    public static String CERTIFICATE_PRICE = CERTIFICATE + "price";
    public static String CERTIFICATE_DURATION = CERTIFICATE + "duration";
    public static String CERTIFICATE_CREATE_DATE = CERTIFICATE + "create_date";
    public static String CERTIFICATE_LAST_UPDATE_DATE = CERTIFICATE + "last_update_date";

    /* general constants */
    public static final int SINGLE_ENTITY = 0;
    public static final int ZERO_ROWS_NUMBER = 0;
    public static final int SINGLE_ROW = 1;

    private GeneralConstant() {
    }
}