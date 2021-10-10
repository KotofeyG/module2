package com.epam.esm.gift_system.repository.dao.mapper;

import com.epam.esm.gift_system.repository.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.*;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(rs.getLong(CERTIFICATE_ID));
            certificate.setName(rs.getString(CERTIFICATE_NAME));
            certificate.setDescription(rs.getString(CERTIFICATE_DESCRIPTION));
            certificate.setPrice(rs.getBigDecimal(CERTIFICATE_PRICE));
            certificate.setDuration(rs.getInt(CERTIFICATE_DURATION));
            certificate.setCreateDate(LocalDateTime.parse(rs.getString(CERTIFICATE_CREATE_DATE), FORMATTER));
            certificate.setLastUpdateDate(LocalDateTime.parse(rs.getString(CERTIFICATE_LAST_UPDATE_DATE), FORMATTER));
        return certificate;
    }
}