package com.epam.esm.gift_system.repository.dao;

import com.epam.esm.gift_system.repository.model.AbstractEntity;

import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    T create(T t);

    Optional<T> findById(Long id);

    boolean delete(Long id);
}