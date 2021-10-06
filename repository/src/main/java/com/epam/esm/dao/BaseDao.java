package com.epam.esm.dao;

import com.epam.esm.model.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T extends AbstractEntity> {
    int FIRST_PARAM_INDEX = 1;
    int SECOND_PARAM_INDEX = 2;
    int THIRD_PARAM_INDEX = 3;
    int FOURTH_PARAM_INDEX = 4;
    int FIFTH_PARAM_INDEX = 5;
    int SIXTH_PARAM_INDEX = 6;

    T insert(T t);

    Optional<T> findById(Long id);

    Optional<T> findByName(String name);

    List<T> findAll();

    boolean delete(Long id);
}