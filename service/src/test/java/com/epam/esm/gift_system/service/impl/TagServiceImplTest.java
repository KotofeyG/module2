package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.repository.dao.impl.TagDaoImpl;
import com.epam.esm.gift_system.repository.model.Tag;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.converter.TagToDtoConverter;
import com.epam.esm.gift_system.service.dto.TagDto;
import com.epam.esm.gift_system.service.exception.*;
import com.epam.esm.gift_system.service.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.gift_system.service.validator.EntityValidator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final long TAG_ID = 1;
    private static final String TAG_NAME = "Tag";

    private Tag tag;
    private TagDto expected;
    private List<Tag> tagList;
    private List<TagDto> expectedList;

    @InjectMocks
    private TagServiceImpl service;
    @Mock
    private TagDaoImpl tagDao;
    @Mock
    private EntityValidator validator;
    @Mock
    private TagToDtoConverter toTagDtoConverter;
    @Mock
    private DtoToTagConverter toTagConverter;

    @BeforeEach
    void setUp() {
        tag = new Tag(TAG_ID, TAG_NAME);
        expected = new TagDto(TAG_ID, TAG_NAME);
        tagList = List.of(tag, tag, tag);
        expectedList = List.of(expected, expected, expected);
    }

    @Test
    void create() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(false).when(tagDao).isExisting(Mockito.anyString());
        doReturn(expected).when(toTagDtoConverter).convert(Mockito.any(Tag.class));
        doReturn(tag).when(tagDao).create(Mockito.any(Tag.class));
        doReturn(tag).when(toTagConverter).convert(Mockito.any(TagDto.class));
        TagDto actual = service.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void createThrowExceptionWhenArgInvalid() {
        doReturn(false).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityNotValidTagNameException");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void createThrowExceptionWhenTagAlreadyExists() {
        doReturn(true).when(validator).isNameValid(Mockito.anyString(), Mockito.any(ValidationType.class));
        doReturn(true).when(tagDao).isExisting(Mockito.anyString());
        try {
            service.create(expected);
            fail("Method insert should throw exception EntityAlreadyExistsException");
        } catch (EntityAlreadyExistsException e) {
            assertTrue(true);
        }
    }

    @Test
    void findById() {
        doReturn(Optional.of(tag)).when(tagDao).findById(Mockito.anyLong());
        doReturn(expected).when(toTagDtoConverter).convert(Mockito.any(Tag.class));
        TagDto actual = service.findById(TAG_ID);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdThrowExceptionResourceDoesntExist() {
        doReturn(Optional.empty()).when(tagDao).findById(Mockito.anyLong());
        try {
            service.findById(TAG_ID);
            fail("Method findById should throw exception EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void findAll() {
        doReturn(tagList).when(tagDao).findAll();
        doReturn(expected).when(toTagDtoConverter).convert(Mockito.any(Tag.class));
        List<TagDto> actualList = service.findAll();
        assertEquals(expectedList, actualList);
    }

    @Test
    void findAllReturnsEmptyList() {
        doReturn(List.of()).when(tagDao).findAll();
        List<TagDto> actualList = service.findAll();
        assertEquals(List.of(), actualList);
    }

    @Test
    void delete() {
        doReturn(Optional.of(tag)).when(tagDao).findById(Mockito.anyLong());
        doReturn(expected).when(toTagDtoConverter).convert(Mockito.any(Tag.class));
        doReturn(false).when(tagDao).isUsed(Mockito.anyLong());
        doReturn(true).when(tagDao).delete(Mockito.anyLong());
        TagDto actual = service.delete(TAG_ID);
        assertEquals(expected, actual);
    }

    @Test
    void deleteThrowExceptionWhenResourceDoesntExist() {
        doReturn(Optional.empty()).when(tagDao).findById(Mockito.anyLong());
        try {
            service.delete(TAG_ID);
            fail("Method delete should throw exception EntityNotFoundException");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void deleteThrowExceptionWhenResourceInUsed() {
        doReturn(Optional.of(tag)).when(tagDao).findById(Mockito.anyLong());
        doReturn(expected).when(toTagDtoConverter).convert(Mockito.any(Tag.class));
        doReturn(true).when(tagDao).isUsed(Mockito.anyLong());
        try {
            service.delete(TAG_ID);
            fail("Method delete should throw exception EntityIsUsedException");
        } catch (EntityIsUsedException e) {
            assertTrue(true);
        }
    }

    @Test
    void deleteThrowExceptionWhenResourceCannotBeDeleted() {
        doReturn(Optional.of(tag)).when(tagDao).findById(Mockito.anyLong());
        doReturn(expected).when(toTagDtoConverter).convert(Mockito.any(Tag.class));
        doReturn(false).when(tagDao).isUsed(Mockito.anyLong());
        doReturn(false).when(tagDao).delete(Mockito.anyLong());
        try {
            service.delete(TAG_ID);
            fail("Method delete should throw exception InternalServerException");
        } catch (InternalServerException e) {
            assertTrue(true);
        }
    }
}