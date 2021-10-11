package com.epam.esm.gift_system.service.impl;

import com.epam.esm.gift_system.repository.dao.impl.TagDaoImpl;
import com.epam.esm.gift_system.repository.model.Tag;
import com.epam.esm.gift_system.service.converter.DtoToTagConverter;
import com.epam.esm.gift_system.service.converter.TagToDtoConverter;
import com.epam.esm.gift_system.service.dto.TagDto;
import com.epam.esm.gift_system.service.exception.EntityAlreadyExistsException;
import com.epam.esm.gift_system.service.exception.EntityIsUsedException;
import com.epam.esm.gift_system.service.exception.EntityNotFoundException;
import com.epam.esm.gift_system.service.exception.EntityNotValidTagNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    TagServiceImpl tagService;
    TagDaoImpl tagDao;
    TagToDtoConverter tagToDtoConverter;
    DtoToTagConverter dtoToTagConverter;
    Tag tag;
    TagDto expected;

    {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("TagTestName");
        expected = new TagDto();
        expected.setId(1L);
        expected.setName("TagTestName");
        tagToDtoConverter = new TagToDtoConverter();
        dtoToTagConverter = new DtoToTagConverter();
    }

    @BeforeEach
    void setUp() {
        tagDao = mock(TagDaoImpl.class);
        tagService = new TagServiceImpl(tagDao, tagToDtoConverter, dtoToTagConverter);
    }

    @Test
    void insertShouldInsertNewTag() {
        doReturn(tag).when(tagDao).insert(tag);
        TagDto actual = tagService.insert(expected);
        assertEquals(expected, actual);
    }

    @Test
    void insertShouldThrowExceptionWithNullArg() {
        try {
            tagService.insert(null);
            fail("TagServiceImpl.insert(TagDto tagDto) should throw an exception with null argument");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithEmptyArg() {
        try {
            tagService.insert(new TagDto(1L, ""));
            fail("TagServiceImpl.insert(TagDto tagDto) should throw an exception with empty argument");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithInvalidArg() {
        try {
            tagService.insert(new TagDto(1L, "<TagTestName>"));
            fail("TagServiceImpl.insert(TagDto tagDto) should throw an exception with invalid argument");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithLongWordArg() {
        try {
            tagService.insert(new TagDto(1L, "a".repeat(76)));
            fail("TagServiceImpl.insert(TagDto tagDto) should throw an exception with long word argument");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithLetterArg() {
        try {
            tagService.insert(new TagDto(1L, "a"));
            fail("TagServiceImpl.insert(TagDto tagDto) should throw an exception with letter argument");
        } catch (EntityNotValidTagNameException e) {
            assertTrue(true);
        }
    }

    @Test
    void insertShouldThrowExceptionWithExistingArg() {
        try {
            doReturn(true).when(tagDao).isExists(tag.getName());
            tagService.insert(expected);
            fail("TagServiceImpl.insert(TagDto tagDto) should throw an exception with existing argument");
        } catch (EntityAlreadyExistsException e) {
            assertTrue(true);
        }
    }

    @Test
    void findByIdShouldFinishSuccessfully() {
        doReturn(Optional.of(tag)).when(tagDao).findById(tag.getId());
        TagDto actual = tagService.findById(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void findByIdShouldThrowException() {
        try {
            doReturn(Optional.empty()).when(tagDao).findById(Mockito.any());
            tagService.findById(expected.getId());
            fail("TagServiceImpl.findById(Long id) should throw an exception with non-existing argument");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void findAllShouldFinishSuccessfully() {
        List<TagDto> expectedList = List.of(expected, expected, expected);
        doReturn(List.of(tag, tag, tag)).when(tagDao).findAll();
        List<TagDto> actual = tagService.findAll();
        assertEquals(expectedList, actual);
    }

    @Test
    void findAllShouldFinishSuccessfullyWithEmptyList() {
        List<TagDto> expectedList = new ArrayList<>();
        doReturn(new ArrayList<>()).when(tagDao).findAll();
        List<TagDto> actual = tagService.findAll();
        assertEquals(expectedList, actual);
    }

    @Test
    void deleteShouldDeleteTag() {
        doReturn(true).when(tagDao).delete(tag.getId());
        doReturn(Optional.of(tag)).when(tagDao).findById(tag.getId());
        TagDto actual = tagService.delete(expected.getId());
        assertEquals(expected, actual);
    }

    @Test
    void deleteShouldThrowExceptionWithNonExistingArg() {
        doReturn(Optional.empty()).when(tagDao).findById(tag.getId());
        try {
            tagService.delete(expected.getId());
            fail("TagServiceImpl.delete(Long id) should throw an exception with non-existing argument");
        } catch (EntityNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    void deleteShouldThrowExceptionWithUsingArg() {
        doReturn(Optional.of(tag)).when(tagDao).findById(Mockito.any(Long.class));
        doReturn(true).when(tagDao).isUsed(tag.getId());
        try {
            tagService.delete(expected.getId());
            fail("TagServiceImpl.delete(Long id) should throw an exception with non-existing argument");
        } catch (EntityIsUsedException e) {
            assertTrue(true);
        }
    }
}