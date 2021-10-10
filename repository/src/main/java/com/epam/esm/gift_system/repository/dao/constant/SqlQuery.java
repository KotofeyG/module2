package com.epam.esm.gift_system.repository.dao.constant;

public class SqlQuery {
    public static final String ORDER_BY_ID = " ORDER BY id";

    /* Requests to 'tags' table into gift_certificates_system database */
    public static final String FIND_ALL_TAGS = "SELECT id, name FROM tags";
    public static final String FIND_TAG_BY_ID = FIND_ALL_TAGS + " WHERE id=?";
    public static final String FIND_TAG_BY_NAME = FIND_ALL_TAGS + " WHERE name=?";
    public static final String INSERT_NEW_TAG = "INSERT INTO tags (name) VALUES (?)";
    public static final String DELETE_TAG_BY_ID = "DELETE FROM tags WHERE id=?";
    public static final String COUNT_TAG_BY_NAME = "SELECT count(*) FROM tags WHERE name=?";
    public static final String COUNT_TAG_USAGE = "SELECT count(*) FROM tags_certificates WHERE tag_id=?";

    /* Requests to 'gift_certificates' table into gift_certificates_system database */
    public static final String INSERT_NEW_GIFT_CERTIFICATE = "INSERT INTO gift_certificates (name, description, price," +
            " duration, create_date, last_update_date) VALUES (?,?,?,?,?,?)";
    public static final String FIND_ALL_GIFT_CERTIFICATES = "SELECT gift_certificates.id, gift_certificates.name" +
            ", description, price, duration, create_date, last_update_date, tags.id, tags.name FROM gift_certificates" +
            " JOIN tags_certificates ON gift_certificate_id=gift_certificates.id JOIN tags ON tags.id=tag_id";
    public static final String FIND_GIFT_CERTIFICATE_BY_ID = FIND_ALL_GIFT_CERTIFICATES + " WHERE gift_certificates.id=?";
    public static final String FIND_GIFT_CERTIFICATE_BY_NAME = FIND_ALL_GIFT_CERTIFICATES + " WHERE gift_certificates.name=?";
    public static final String ADD_TAG_TO_GIFT_CERTIFICATE = "INSERT INTO tags_certificates" +
            " (gift_certificate_id, tag_id) VALUES (?, ?)";
    public static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificates WHERE id=?";
    public static final String DELETE_TAG_FROM_CERTIFICATE = "DELETE FROM tags_certificates WHERE gift_certificate_id=?" +
            " AND tag_id=?";

    private SqlQuery() {
    }
}