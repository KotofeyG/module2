package com.epam.esm.gift_system.service.util;

import com.epam.esm.gift_system.service.dto.GiftCertificateDto;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.epam.esm.gift_system.repository.model.EntityField.*;

public class MapperUtils {
    public static Map<String, Object> giftCertificateDtoToMap(GiftCertificateDto certificateDto) {
        Map<String, Object> fieldList = new LinkedHashMap<>();
        if (certificateDto.getName() != null) {
            fieldList.put(NAME.toString(), certificateDto.getName());
        }
        if (certificateDto.getDescription() != null) {
            fieldList.put(DESCRIPTION.toString(), certificateDto.getDescription());
        }
        if (certificateDto.getPrice() != null) {
            fieldList.put(PRICE.toString(), certificateDto.getPrice());
        }
        if (certificateDto.getDuration() != 0) {
            fieldList.put(DURATION.toString(), certificateDto.getDuration());
        }

        return fieldList;
    }

    private MapperUtils() {
    }
}