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
    public Tag insert(Tag tag) {
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
        Optional<Tag> optionalTag;
        try {
            optionalTag = Optional.of(jdbcTemplate.queryForObject(FIND_TAG_BY_ID, new TagRowMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            optionalTag = Optional.empty();
        }
        return optionalTag;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Optional<Tag> optionalTag;
        try {
            optionalTag = Optional.of(jdbcTemplate.queryForObject(FIND_TAG_BY_NAME, new TagRowMapper(), name));
        } catch (EmptyResultDataAccessException e) {
            optionalTag = Optional.empty();
        }
        return optionalTag;
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
    public boolean isExists(String name) {
        return jdbcTemplate.queryForObject(COUNT_TAG_BY_NAME, Integer.class, name) > ZERO_ROWS_NUMBER;
    }

    @Override
    public boolean isUsed(Long id) {
        return jdbcTemplate.queryForObject(COUNT_TAG_USAGE, Integer.class, id) > ZERO_ROWS_NUMBER;
    }

    @Override
    public Tag findOrCreateTag(Tag tag) {
        Optional<Tag> optionalTag = findByName(tag.getName());
        if (optionalTag.isEmpty()) {
            return insert(tag);
        }
        return optionalTag.get();
    }
}