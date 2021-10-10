package com.epam.esm.gift_system.repository.dao.mapper;

import com.epam.esm.gift_system.repository.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.gift_system.repository.dao.constant.GeneralConstant.*;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(TAG_ID));
        tag.setName(rs.getString(TAG_NAME));
        return tag;
    }
}