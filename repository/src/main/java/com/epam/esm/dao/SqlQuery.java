package com.epam.esm.dao;

public class SqlQuery {
    public static final String ORDER_BY_ID = " ORDER BY id";

    /* Requests to 'tags' table into gift_certificates_system database */
    public static String FIND_ALL_TAGS = "SELECT id, name FROM tags";
    public static String FIND_TAG_BY_ID = FIND_ALL_TAGS + " WHERE id=?";
    public static String FIND_TAG_BY_NAME = FIND_ALL_TAGS + " WHERE name=?";
    public static String INSERT_NEW_TAG = "INSERT INTO tags (name) VALUES (?)";
    public static String DELETE_TAG_BY_ID = "DELETE FROM tags WHERE id=?";
    public static String COUNT_TAG_BY_ID = "SELECT count(*) FROM tags WHERE id=?";
    public static String COUNT_TAG_BY_NAME = "SELECT count(*) FROM tags WHERE name=?";
    public static String COUNT_TAG_USAGE = "SELECT count(*) FROM tags_certificates WHERE tag_id=?";

    /* Requests to 'gift_certificates' table into gift_certificates_system database */
    public static String INSERT_NEW_GIFT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price," +
            " duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";
    public static String FIND_ALL_GIFT_CERTIFICATES = "SELECT id, name, description, price, duration, create_date, " +
            "last_update_date FROM gift_certificates";
    public static String FIND_GIFT_CERTIFICATE_BY_ID = FIND_ALL_GIFT_CERTIFICATES + " WHERE id=?";
    public static String FIND_GIFT_CERTIFICATE_BY_NAME = FIND_ALL_GIFT_CERTIFICATES + " WHERE name=?";

    private SqlQuery() {
    }
}