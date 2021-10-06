package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller provides service within Tag.class entities.
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Is used for inserting new Tag
     *
     * @return TagDto just inserted
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto insert(@RequestBody TagDto tagDto) {
        return tagService.insert(tagDto);
    }

    /**
     * Is used for getting tag by ID
     *
     * @return TagDto
     */
    @GetMapping("/{id}")
    public TagDto findById(@PathVariable Long id) {
        return tagService.findById(id);
    }

    /**
     * Is used for getting list of tags available
     *
     * @return List<TagDto> the list of certificates
     */
    @GetMapping
    public List<TagDto> findAll() {
        return tagService.findAll();
    }

    /**
     * Is used for Removing Tag by ID given
     *
     * @param id the id of Tag to remove
     */
    @DeleteMapping("/{id}")
    public TagDto deleteTag(@PathVariable Long id) {
        return tagService.delete(id);
    }
}