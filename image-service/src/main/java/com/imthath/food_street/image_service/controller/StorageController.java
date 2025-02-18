package com.imthath.food_street.image_service.controller;

import com.imthath.food_street.image_service.service.StorageService;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/storage")
@Validated
public class StorageController {
    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/signed-url")
    @ResponseStatus(HttpStatus.CREATED)
    public String createSignedUrl(@RequestParam @NotBlank String fileExtension) {
        return storageService.createSignedUrl(fileExtension);
    }

    @GetMapping("/signed-url")
    public ResponseEntity<String> fetchSignedUrl(@RequestParam @NotBlank String fileName, @RequestParam @NotBlank String fileExtension) {
        return ResponseEntity.ok(storageService.fetchSignedUrl(fileName, fileExtension));
    }

    @GetMapping("/host")
    public ResponseEntity<String> getStorageHost(@RequestParam(required = false) String context) {
        return ResponseEntity.ok(storageService.getStorageHost(context));
    }
} 