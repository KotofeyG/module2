package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.service.exception.*;
import com.epam.esm.gift_system.service.TagService;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.converter.TagToDtoConverter;
import com.epam.esm.gift_system.repository.dao.TagDao;
import com.epam.esm.gift_system.service.dto.TagDto;
import com.epam.esm.gift_system.service.util.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.gift_system.service.util.validator.EntityValidator.ValidationType.*;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;
    private final EntityValidator validator;
    private final TagToDtoConverter toTagDtoConverter;
    private final DtoToTagConverter toTagConverter;

    @Autowired
    public TagServiceImpl(TagDao tagDao, EntityValidator validator, TagToDtoConverter toTagDtoConverter
            , DtoToTagConverter toTagConverter) {
        this.tagDao = tagDao;
        this.validator = validator;
        this.toTagDtoConverter = toTagDtoConverter;
        this.toTagConverter = toTagConverter;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        if (validator.isNameValid(tagDto.getName(), INSERT)) {
            if (!tagDao.isExisting(tagDto.getName())) {
                return toTagDtoConverter.convert(tagDao.create(toTagConverter.convert(tagDto)));
            }
            throw new EntityAlreadyExistsException();
        }
        throw new EntityNotValidTagNameException();
    }

    @Override
    public TagDto findById(Long id) {
        return tagDao.findById(id).map(toTagDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<TagDto> findAll() {
        return tagDao.findAll().stream().map(toTagDtoConverter::convert).collect(Collectors.toList());
    }

    @Override
    public TagDto delete(Long id) {
        TagDto deletedTag = findById(id);
        if (tagDao.isUsed(id)) {
            throw new EntityIsUsedException();
        }
        if (!tagDao.delete(id)) {
            throw new InternalServerException();
        }
        return deletedTag;
    }
}