package com.epam.esm.gift_system.repository.dao.impl;

import com.epam.esm.gift_system.repository.dao.GiftCertificateDao;
import com.epam.esm.gift_system.repository.dao.SqlQueryBuilder;
import com.epam.esm.gift_system.repository.dao.TagDao;
import com.epam.esm.gift_system.repository.dao.mapper.GiftCertificateExtractor;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.*;
import static com.epam.esm.gift_system.repository.dao.constant.SqlQuery.*;
import static com.epam.esm.gift_system.repository.model.EntityField.LAST_UPDATE_DATE;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateExtractor extractor;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, GiftCertificateExtractor extractor, TagDao tagDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_GIFT_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(FIRST_PARAM_INDEX, giftCertificate.getName());
            ps.setString(SECOND_PARAM_INDEX, giftCertificate.getDescription());
            ps.setBigDecimal(THIRD_PARAM_INDEX, giftCertificate.getPrice());
            ps.setInt(FOURTH_PARAM_INDEX, giftCertificate.getDuration());
            ps.setTimestamp(FIFTH_PARAM_INDEX, Timestamp.valueOf(giftCertificate.getCreateDate()));
            ps.setTimestamp(SIXTH_PARAM_INDEX, Timestamp.valueOf(giftCertificate.getLastUpdateDate()));
            return ps;
        }, keyHolder);

        giftCertificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return giftCertificate;
    }

    @Override
    public void update(Long id, Map<String, Object> updatedFields) {
        updatedFields.put(LAST_UPDATE_DATE.toString(), LocalDateTime.now());
        String query = SqlQueryBuilder.buildCertificateQueryForUpdate(updatedFields.keySet());
        List<Object> args = new ArrayList<>(updatedFields.values());
        args.add(id);
        jdbcTemplate.update(query, args.toArray());
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        List<GiftCertificate> giftCertificate = jdbcTemplate.query(FIND_GIFT_CERTIFICATE_BY_ID, extractor, id);
        return !giftCertificate.isEmpty() ? Optional.of(giftCertificate.get(SINGLE_ENTITY)) : Optional.empty();
    }

    @Override
    public List<GiftCertificate> findByAttributes(String tagName, String searchPart, List<String> sortingFieldList, String orderSort) {
        String query = SqlQueryBuilder.buildCertificateQueryForSearchAndSort(tagName, searchPart, sortingFieldList, orderSort);
        return jdbcTemplate.query(query, extractor);
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_ID, id) == SINGLE_ROW;
    }

    @Override
    public boolean isExisting(Long id) {
        return jdbcTemplate.queryForObject(COUNT_CERTIFICATE_BY_ID, Integer.class, id) > ZERO_ROWS_NUMBER;
    }

    @Override
    public List<Tag> addTagsToCertificate(Long id, List<Tag> addedTagList) {
        addedTagList = addedTagList.stream().map(tagDao::findOrCreateTag).toList();
        for (Tag tag : addedTagList) {
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(ADD_TAG_TO_GIFT_CERTIFICATE);
                statement.setLong(FIRST_PARAM_INDEX, id);
                statement.setLong(SECOND_PARAM_INDEX, tag.getId());
                return statement;
            });
        }
        return addedTagList;
    }

    @Override
    public void deleteAllTagsFromCertificate(Long id) {
        jdbcTemplate.update(DELETE_ALL_TAGS_FROM_CERTIFICATE);
    }
}