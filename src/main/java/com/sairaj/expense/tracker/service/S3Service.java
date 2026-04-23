package com.sairaj.expense.tracker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Slf4j
@Service
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    private S3Presigner s3Presigner;

    private S3Client s3Client;

    S3Service(S3Client s3Client,S3Presigner s3Presigner){
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    public void uploadByteArray(byte[] fileData, String fileName, String contentType) {

        RequestBody requestBody = RequestBody.fromBytes(fileData);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)              // S3 object key (path + filename)
                .contentType(contentType)   // e.g., "image/png", "application/pdf"
                .contentLength((long) fileData.length)
                .build();
        try {
            s3Client.putObject(putObjectRequest, requestBody);
        }catch(Exception e){
            log.info("PDF uploading failed");
            throw e;
        }
    }

    public boolean fileExists(String fileName){
        try{
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            if(e.statusCode()==404){
                return false;
            }
            throw e;
        }
    }

    public String retriveFile(String fileName){
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.utilities().getUrl(getUrlRequest).toExternalForm();
    }
}
