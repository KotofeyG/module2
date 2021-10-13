package com.epam.esm.gift_system.repository.dao;

import com.epam.esm.gift_system.repository.model.GiftCertificate;
import com.epam.esm.gift_system.repository.model.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateDao extends BaseDao<GiftCertificate>{
    boolean isExisting(Long id);

    void update(Long id, Map<String, Object> updatedFields);

    List<Tag> addTagsToCertificate(Long id, List<Tag> addedTagList);

    void deleteAllTagsFromCertificate(Long id);

    List<GiftCertificate> findByAttributes(String tagName, String searchPart, List<String> sortingFieldList, String orderSort);
}