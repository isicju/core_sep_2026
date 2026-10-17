package com.kotojava.lesson3.repository;

import com.kotojava.lesson3.model.LoanApplication;
import com.kotojava.lesson3.service.LoanPdfService;
import com.kotojava.lesson3.service.PdfGeneratorOl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Repository
@AllArgsConstructor
public class S3Repository {

    private S3Client s3Client;

    public void save(byte[] fileBytes, String fileName) {
        s3Client.putObject(
                PutObjectRequest.builder().bucket(S3Config.BUCKET_NAME).key(fileName).build(),
                RequestBody.fromBytes(fileBytes));
    }

}
