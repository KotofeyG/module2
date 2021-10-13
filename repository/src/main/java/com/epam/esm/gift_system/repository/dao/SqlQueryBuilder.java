package com.epam.esm.gift_system.repository.dao;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.CERTIFICATE;
import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.CERTIFICATE_ID;
import static com.epam.esm.gift_system.repository.dao.constant.SqlQuery.*;

public class SqlQueryBuilder {
    private static final String DEFAULT_SORT = "ASC";
    private static final String EMPTY = "";
    private static final String SPACE = " ";
    private static final String COMMA_SIGN = ", ";

    public static String buildCertificateQueryForUpdate(Set<String> columnNames) {
        String res = START_OF_UPDATE_QUERY + MIDDLE_OF_UPDATE_QUERY.repeat(columnNames.size()-1) + END_OF_UPDATE_QUERY;
        return res.formatted(columnNames.toArray());
    }

    public static String buildCertificateQueryForSearchAndSort(String tagName, String searchPart, List<String> sortingFieldList, String orderSort) {
        tagName = tagName != null ? tagName : EMPTY;
        searchPart = searchPart != null ? searchPart : EMPTY;
        String queryMainPart = String.format(SEARCH_AND_SORT_QUERY, tagName, searchPart, searchPart);
        StringBuilder resultQuery = new StringBuilder(queryMainPart);

        if (sortingFieldList != null && !sortingFieldList.isEmpty()) {
            sortingFieldList.forEach(field -> resultQuery
                    .append(CERTIFICATE)
                    .append(field)
                    .append(SPACE)
                    .append(Objects.requireNonNullElse(orderSort, DEFAULT_SORT))
                    .append(COMMA_SIGN));
            resultQuery.deleteCharAt(resultQuery.length() - 2);
        } else {
            resultQuery.append(CERTIFICATE_ID).append(SPACE).append(Objects.requireNonNullElse(orderSort, DEFAULT_SORT));
        }
        return resultQuery.toString();
    }
}