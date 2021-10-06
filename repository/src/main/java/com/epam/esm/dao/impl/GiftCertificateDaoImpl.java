package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.epam.esm.dao.SqlQuery.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GiftCertificate insert(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_NEW_GIFT_CERTIFICATE
                    , Statement.RETURN_GENERATED_KEYS);
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
    public Optional<GiftCertificate> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_GIFT_CERTIFICATE_BY_ID
                , new BeanPropertyRowMapper<>(GiftCertificate.class), id));
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_GIFT_CERTIFICATE_BY_NAME
                , new BeanPropertyRowMapper<>(GiftCertificate.class), name));
    }

    @Override
    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(FIND_ALL_GIFT_CERTIFICATES, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public boolean delete(Long id) {                                                //todo
        return false;
    }
}