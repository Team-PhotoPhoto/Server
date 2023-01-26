package com.hjin.photophoto.service.postImg;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AbstractAmazonS3;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class PostsImgService {
    @Value("${s3.bucket.name}")
    static String BUCKET_NAME;
    AmazonS3 amazonS3;

    @Transactional
    public String signBucket(String folder, Long keyName) {
        String bucketName = BUCKET_NAME + folder;
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(folder, Long.toString(keyName));
        System.out.println(folder);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
//        generatePresignedUrlRequest.addRequestParameter(
//                Headers.S3_CANNED_ACL,
//                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
//        log.info(expiration.toString());
        return expiration;
    }

}
