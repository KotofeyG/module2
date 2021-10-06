package com.epam.esm.service.impl;

import com.epam.esm.converter.DtoToGiftCertificateConverter;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.converter.GiftCertificateToDtoConverter;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateToDtoConverter toDtoConverter;
    private final DtoToGiftCertificateConverter toGiftCertificateConverter;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, GiftCertificateToDtoConverter toDtoConverter
            , DtoToGiftCertificateConverter toGiftCertificateConverter) {
        this.giftCertificateDao = giftCertificateDao;
        this.toDtoConverter = toDtoConverter;
        this.toGiftCertificateConverter = toGiftCertificateConverter;
    }

    @Override
    public GiftCertificateDto insert(GiftCertificateDto giftCertificateDto) {
        return toDtoConverter.convert(giftCertificateDao.insert(toGiftCertificateConverter.convert(giftCertificateDto)));
    }

    @Override
    public List<GiftCertificateDto> findAll() {
        return giftCertificateDao.findAll().stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto findById(long id) {
        return giftCertificateDao.findById(id).map(toDtoConverter::convert).orElseThrow();
    }

    @Override
    public GiftCertificateDto findByName(String name) {
        return giftCertificateDao.findByName(name).map(toDtoConverter::convert).orElseThrow();
    }

    @Override
    public boolean delete(long id) {                                                //todo
        return false;
    }
}