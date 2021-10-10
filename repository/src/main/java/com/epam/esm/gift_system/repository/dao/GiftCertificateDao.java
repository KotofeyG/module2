package com.epam.esm.gift_system.repository.dao;

import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDao extends BaseDao<GiftCertificate>{
    boolean update(Long id, Map<String, Object> fields);

    void addTagsToCertificate(Long id, List<Tag> tags);

    void deleteTagsFromCertificate(Long id, List<Tag> tags);

    List<GiftCertificate> findByAttributes(String tagName, String searchPart, List<String> sortingFields, String orderSort);
}