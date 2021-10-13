package com.epam.esm.gift_system.repository.dao;

import com.epam.esm.gift_system.repository.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends BaseDao<Tag>{
    Optional<Tag> findByName(String name);

    Tag findOrCreateTag(Tag tag);

    List<Tag> findAll();

    boolean isExisting(String name);

    boolean isUsed(Long id);
}