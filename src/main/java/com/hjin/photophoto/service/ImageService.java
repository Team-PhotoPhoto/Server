package com.hjin.photophoto.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.hjin.photophoto.exception.MyException;
import com.hjin.photophoto.exception.MyExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ImageService {


    @Value("${s3.bucket.name.posts}")
    String POST_BUCKET_NAME;


    @Value("${s3.bucket.name.user}")
    String USER_BUCKET_NAME;
    private final AmazonS3Client amazonS3Client;


    public String getSingedUrl(int minutes, String type, Long id, HttpMethod method) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000L * 60 * minutes;     //min
        expiration.setTime(expTimeMillis);

        String bucketName = "";
        String key = "";

        if (Objects.equals(type, "user")){
            bucketName = USER_BUCKET_NAME;
            key = id + ".png";
        }
        else if (Objects.equals(type, "thumbnail")){
            bucketName = POST_BUCKET_NAME;
            key = "thumbnail/" + id + ".png";
        }
        else if (Objects.equals(type, "origin")){
            bucketName = POST_BUCKET_NAME;
            key = "origin/" + id + ".png";
        } else {
            throw new MyException(MyExceptionType.NOT_EXIST_BUCKET_TYPE, id);
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(method)
                        .withExpiration(expiration);
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }


}
