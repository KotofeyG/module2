package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto insert(TagDto tagDto);

    TagDto findById(Long id);

    TagDto findByName(String name);

    List<TagDto> findAll();

    TagDto delete(Long id);
}