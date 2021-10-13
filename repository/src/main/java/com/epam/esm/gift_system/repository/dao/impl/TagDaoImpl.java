package com.epam.esm.gift_system.repository.dao.impl;

import com.epam.esm.gift_system.repository.dao.TagDao;
import com.epam.esm.gift_system.repository.dao.mapper.TagRowMapper;
import com.epam.esm.gift_system.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.*;
import static com.epam.esm.gift_system.repository.dao.constant.SqlQuery.*;

@Repository
public class TagDaoImpl implements TagDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_TAG, Statement.RETURN_GENERATED_KEYS);
            ps.setString(FIRST_PARAM_INDEX, tag.getName());
            return ps;
        }, keyHolder);
        tag.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return tag;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return findBy(id.toString(), FIND_TAG_BY_ID);
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_TAGS + ORDER_BY_ID, new TagRowMapper());
    }

    @Override
    public boolean delete(Long id) {
        return jdbcTemplate.update(DELETE_TAG_BY_ID, id) == SINGLE_ROW;
    }

    @Override
    public boolean isExisting(String name) {
        return jdbcTemplate.queryForObject(COUNT_TAG_BY_NAME, Integer.class, name) > ZERO_ROWS_NUMBER;
    }

    @Override
    public boolean isUsed(Long id) {
        return jdbcTemplate.queryForObject(COUNT_TAG_USAGE, Integer.class, id) > ZERO_ROWS_NUMBER;
    }

    @Override
    public Tag findOrCreateTag(Tag tag) {
        return findBy(tag.getName(), FIND_TAG_BY_NAME).orElseGet(() -> create(tag));
    }

    private Optional<Tag> findBy(String field, String sqlQuery) {
        Optional<Tag> optionalTag;
        try {
            optionalTag = Optional.of(jdbcTemplate.queryForObject(sqlQuery, new TagRowMapper(), field));
        } catch (EmptyResultDataAccessException e) {
            optionalTag = Optional.empty();
        }
        return optionalTag;
    }
}