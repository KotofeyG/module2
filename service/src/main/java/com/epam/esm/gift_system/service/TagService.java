package com.epam.esm.gift_system.service;

import com.epam.esm.gift_system.service.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto insert(TagDto tagDto);

    TagDto findById(Long id);

    List<TagDto> findAll();

    TagDto delete(Long id);
}