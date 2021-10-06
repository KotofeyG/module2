package com.epam.esm.dao;

import com.epam.esm.model.Tag;

public interface TagDao extends BaseDao<Tag>{
    boolean isExists(Long id);

    boolean isExists(String name);

    boolean isUsed(Long id);
}