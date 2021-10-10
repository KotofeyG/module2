package com.epam.esm.gift_system.service.converter;

import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import com.epam.esm.gift_system.repository.model.GiftCertificate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToGiftCertificateConverter implements Converter<GiftCertificateDto, GiftCertificate> {
    @Override
    public GiftCertificate convert(GiftCertificateDto source) {
        return new GiftCertificate(source.getId(), source.getName()
                , source.getDescription(), source.getPrice(), source.getDuration(), source.getCreateDate()
                , source.getLastUpdateDate(), source.getTags());
    }
}
