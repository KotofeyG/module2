package com.epam.esm.service.impl;

import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityCreationException;
import com.epam.esm.exception.EntityIsUsedException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.converter.DtoToTagConverter;
import com.epam.esm.converter.TagToDtoConverter;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.validator.TagValidator;
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
        if (TagValidator.isNameValid(tagDto.getName())) {
            if (!tagDao.isExists(tagDto.getName())) {
                return tagToDtoConverter.convert(tagDao.insert(dtoToTagConverter.convert(tagDto)));
            }
            throw new EntityAlreadyExistsException();
        }
        throw new EntityCreationException();
    }

    @Override
    public TagDto findById(Long id) {
        return tagDao.findById(id).map(tagToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public TagDto findByName(String name) {
        return tagDao.findByName(name).map(tagToDtoConverter::convert).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<TagDto> findAll() {
        return tagDao.findAll().stream().map(tagToDtoConverter::convert).collect(Collectors.toList());
    }

    @Override
    public TagDto delete(Long id) {
        TagDto deletedTag = findById(id);
        if (!tagDao.isUsed(id)) {
            tagDao.delete(id);
            return deletedTag;
        }
        throw new EntityIsUsedException();
    }
}