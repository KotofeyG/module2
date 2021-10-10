package com.epam.esm.gift_system.web.controller;

import com.epam.esm.gift_system.service.GiftCertificateService;
import com.epam.esm.gift_system.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GiftCertificateDto insert(@RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.insert(giftCertificateDto);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto update(@PathVariable Long id, @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.update(id, giftCertificateDto);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto findById(@PathVariable Long id) {
        return giftCertificateService.findById(id);
    }

    @GetMapping
    public List<GiftCertificateDto> findByAttributes(@RequestParam(required = false, name = "tagName") String tagName,
                                                     @RequestParam(required = false, name = "searchPart") String searchPart,
                                                     @RequestParam(required = false, name = "sortingFields") List<String> sortingFields,
                                                     @RequestParam(required = false, name = "orderSort") String orderSort) {
        return giftCertificateService.findByAttributes(tagName, searchPart, sortingFields, orderSort);
    }

    @DeleteMapping("/{id}")
    public GiftCertificateDto deleteById(@PathVariable Long id) {
        return giftCertificateService.delete(id);
    }
}