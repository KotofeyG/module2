package com.epam.esm.gift_system.repository.dao;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.CERTIFICATE;

public class SqlQueryBuilder {
    private static final String ORDER_BY = " ORDER BY ";
    private static final String DEFAULT_SORT = "ASC";
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String COMMA_SIGN = ", ";
    private static final String START_OF_UPDATE_QUERY = "UPDATE gift_certificates SET ";
    private static final String MIDDLE_OF_UPDATE_QUERY = "%s=?, ";
    private static final String END_OF_UPDATE_QUERY = " %s=? WHERE id=?";
    private static final String SEARCH_AND_SORT_QUERY = "SELECT gift_certificates.id, gift_certificates.name" +
            ", description, price, duration, create_date, last_update_date, tags.id, tags.name FROM gift_certificates" +
            " JOIN tags_certificates ON gift_certificate_id=gift_certificates.id JOIN tags ON tag_id=tags.id" +
            " WHERE tags.name LIKE ('%%' '%s' '%%') AND gift_certificates.name LIKE CONCAT ('%%', '%s', '%%') " +
            "AND gift_certificates.description LIKE CONCAT ('%%', '%s', '%%')";

    public static String buildCertificateQueryForUpdate(Set<String> columnNames) {
        String res = START_OF_UPDATE_QUERY + MIDDLE_OF_UPDATE_QUERY.repeat(columnNames.size()-1) + END_OF_UPDATE_QUERY;
        return res.formatted(columnNames.toArray());
    }

    public static String buildCertificateQueryForSearchAndSort(String tagName, String searchPart, List<String> sortingFields, String orderSort) {
        tagName = tagName != null ? tagName : EMPTY;
        searchPart = searchPart != null ? searchPart : EMPTY;
        String result = String.format(SEARCH_AND_SORT_QUERY, tagName, searchPart, searchPart);

        StringBuilder stringBuilder = new StringBuilder(result);

        if (sortingFields != null && !sortingFields.isEmpty()) {
            stringBuilder.append(ORDER_BY);
            sortingFields.forEach(field -> stringBuilder
                    .append(CERTIFICATE)
                    .append(field)
                    .append(SPACE)
                    .append(Objects.requireNonNullElse(orderSort, DEFAULT_SORT))
                    .append(COMMA_SIGN));
            stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        }
        return stringBuilder.toString();
    }
}