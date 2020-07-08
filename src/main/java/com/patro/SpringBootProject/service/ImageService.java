package com.patro.SpringBootProject.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.patro.SpringBootProject.dao.ImageRepository;
import com.patro.SpringBootProject.model.Cart;
import com.patro.SpringBootProject.model.Image;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StatsDClient statsDClient;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;

//    @Value("${amazonProperties.accessKey}")
//    private String accessKey;
//    @Value("${amazonProperties.secretKey}")
//    private String secretKey;

    @Value("${amazonProperties.region}")
    String region;

    @Value("${amazonProperties.profile}")
    String profile;


    private AmazonS3 amazonS3;

    private long startTime;
    private long endTime;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentialsProviderChain awsCredentialsProviderChain = new AWSCredentialsProviderChain(
                new InstanceProfileCredentialsProvider(true),
                new ProfileCredentialsProvider(profile));

        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProviderChain)
                .withRegion(region)
                .build();

    }

    public Set<String> uploadPictures(MultipartFile[] multipartFiles) throws IOException {

        Set<File> files = new HashSet<>();
        Set<String> fileUrls = new HashSet<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String fileUrl = "";
            if (multipartFile.getContentType().equals("image/jpeg") || multipartFile.getContentType().equals("image/png") || multipartFile.getContentType().equals("image/jpg")) {
                File file = convertMultiPartToFile(multipartFile);
                String fileName = generateFileName(multipartFile);
                fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
                fileUrls.add(fileUrl);
                uploadFileTos3bucket(fileName, file);
            }
        }

        return fileUrls;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        startTime = System.currentTimeMillis();
        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("S3 Bucket Upload Picture", endTime - startTime);
    }

    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        startTime = System.currentTimeMillis();
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileName));
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("S3 Bucket Delete Picture", endTime - startTime);
        return "Successfully deleted";
    }

    public void addImages(Image image) {
        startTime = System.currentTimeMillis();
        imageRepository.save(image);
        endTime = System.currentTimeMillis();

    }

    public List<Image> getImagesByBookId(String bookId) {

        return (List<Image>) imageRepository.getImagesByBookId(bookId);
    }

    public void deleteImageByImageId(String imageId) {
        startTime = System.currentTimeMillis();
        imageRepository.deleteImageByImageId(imageId);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB delete image by image Id ",endTime-startTime);
    }

    public Image getImageByImageId(String imageId) {
        return imageRepository.getImageByImageId(imageId);
    }

    public void deleteImage(Image image) {
        startTime = System.currentTimeMillis();
        imageRepository.delete(image);
        endTime = System.currentTimeMillis();
        statsDClient.recordExecutionTime("DB delete image",endTime-startTime);

    }
}