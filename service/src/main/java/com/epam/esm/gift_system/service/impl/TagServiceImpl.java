package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.service.exception.*;
import com.epam.esm.gift_system.service.TagService;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.converter.TagToDtoConverter;
import com.epam.esm.gift_system.repository.dao.TagDao;
import com.epam.esm.gift_system.service.dto.TagDto;
import com.epam.esm.gift_system.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final TagToDtoConverter tagToDtoConverter;
    private final DtoToTagConverter dtoToTagConverter;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagToDtoConverter tagToDtoConverter, DtoToTagConverter dtoToTagConverter) {
        this.tagDao = tagDao;
        this.tagToDtoConverter = tagToDtoConverter;
        this.dtoToTagConverter = dtoToTagConverter;
    }

    @Override
    public TagDto insert(TagDto tagDto) {
        if (tagDto != null && EntityValidator.isNameValid(tagDto.getName())) {
            if (!tagDao.isExists(tagDto.getName())) {
                return tagToDtoConverter.convert(tagDao.insert(dtoToTagConverter.convert(tagDto)));
            }
            throw new EntityAlreadyExistsException();
        }
        throw new EntityNotValidTagNameException();
    }

    @Override
    public TagDto findById(Long id) {
        return tagDao.findById(id).map(tagToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<TagDto> findAll() {
        return tagDao.findAll().stream().map(tagToDtoConverter::convert).collect(Collectors.toList());
    }

    @Override
    public TagDto delete(Long id) {
        TagDto deletedTag = tagDao.findById(id).map(tagToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
        if (tagDao.isUsed(id)) {
            throw new EntityIsUsedException();
        }
        tagDao.delete(id);
        return deletedTag;
    }
}