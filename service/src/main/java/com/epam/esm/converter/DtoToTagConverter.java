package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Tag;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToTagConverter implements Converter<TagDto, Tag> {

    @Override
    public Tag convert(TagDto source) {
        return new Tag(source.getId(), source.getName());
    }
}