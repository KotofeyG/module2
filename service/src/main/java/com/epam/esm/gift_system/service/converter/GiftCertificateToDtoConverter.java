package com.epam.esm.gift_system.service.converter;

import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateToDtoConverter implements Converter<GiftCertificate, GiftCertificateDto> {

    @Override
    public GiftCertificateDto convert(GiftCertificate source) {
        return new GiftCertificateDto(source.getId(), source.getName()
                , source.getDescription(), source.getPrice(), source.getDuration(), source.getCreateDate()
                , source.getLastUpdateDate(), source.getTags());
    }
}