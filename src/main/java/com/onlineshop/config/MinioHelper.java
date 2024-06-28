package com.onlineshop.config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import io.minio.errors.*;

@Slf4j
@Component
public class MinioHelper {

    @Value("${application.minio.url}")
    private String url;
    @Value("${application.minio.username}")
    private String accessKey;
    @Value("${application.minio.password}")
    private String secretKey;
    @Value("${application.minio.bucketName}")
    private String bucketName;

    public String uploadFile(String objectName, MultipartFile data) {
        MinioClient minioClient = MinioClient.builder()
                                    .endpoint(url)
                                    .credentials(accessKey, secretKey)
                                    .build();
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(data.getInputStream(), data.getSize(), -1)
                            .build()
            );
            return ("Uploading file into the bucket Success!");
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | MinioException | IOException e) {
            log.info("Failed to upload file into the bucket");
            return ("Failed to upload file into the bucket");
        }
    }

    public String getPresignedUrl(String objectName, int expiryTimeHours){
        log.info("Get Url Function");
        MinioClient minioClient = MinioClient.builder()
                                    .endpoint(url)
                                    .credentials(accessKey, secretKey)
                                    .build();
        try {
            return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiryTimeHours, TimeUnit.HOURS)
                            .build()
            );
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalArgumentException | MinioException | IOException e) {
            log.info("Failed to get URL" + e.getCause());
            return "Failed to get the URL";
        }
    }

}
