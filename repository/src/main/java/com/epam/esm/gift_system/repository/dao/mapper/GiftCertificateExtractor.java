package com.epam.esm.gift_system.repository.dao.mapper;

import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.*;

@Component
public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {
    private final GiftCertificateRowMapper rowMapper;

    @Autowired
    public GiftCertificateExtractor(GiftCertificateRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, GiftCertificate> giftCertificates = new LinkedHashMap<>();
        while (rs.next()) {
            Long key = rs.getLong(CERTIFICATE_ID);
            giftCertificates.putIfAbsent(key, rowMapper.mapRow(rs, rs.getRow()));
            Tag tag = new Tag(rs.getLong(TAG_ID), rs.getString(TAG_NAME));
            giftCertificates.get(key).addTag(tag);
        }
        return new ArrayList<>(giftCertificates.values());
    }
}