package com.imthath.food_street.image_service.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.imthath.food_street.image_service.config.StorageProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
    private final Storage storage;
    private final StorageProperties storageProperties;

    public StorageService(Storage storage, StorageProperties storageProperties) {
        this.storage = storage;
        this.storageProperties = storageProperties;
    }

    public String createSignedUrl(String fileExtension) {
        String fileName = UUID.randomUUID() + "." + fileExtension;
        BlobId blobId = BlobId.of(storageProperties.bucketName(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", "application/octet-stream");
        
        return storage.signUrl(
            blobInfo,
            storageProperties.signedUrlExpirationMinutes(),
            TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withExtHeaders(extensionHeaders),
            Storage.SignUrlOption.withV4Signature()
        ).toString();
    }

    public String fetchSignedUrl(String fileName, String fileExtension) {
        BlobId blobId = BlobId.of(storageProperties.bucketName(), fileName + "." + fileExtension);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        
        return storage.signUrl(
            blobInfo,
            storageProperties.signedUrlExpirationMinutes(),
            TimeUnit.MINUTES,
            Storage.SignUrlOption.withV4Signature()
        ).toString();
    } 

    public String getStorageHost(String context) {
        return storageProperties.host();
    }
} 