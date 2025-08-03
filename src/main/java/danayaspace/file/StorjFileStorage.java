package danayaspace.file;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.api.ImageResponse;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@Primary
public class StorjFileStorage implements FileStorage {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;
    private final String baseUrl;

    public StorjFileStorage(
            @Value("${storj.access-key}") String accessKey,
            @Value("${storj.secret-key}") String secretKey,
            @Value("${storj.endpoint}") String endpoint,
            @Value("${storj.bucket-name}") String bucketName,
            @Value("${storj.base-url:}") String baseUrl) {

        this.bucketName = bucketName;
        this.baseUrl = baseUrl.isEmpty() ? endpoint : baseUrl;

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        this.s3Client = S3Client.builder()
                .endpointOverride(java.net.URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.US_EAST_1) // Storj uses US_EAST_1 as default
                .forcePathStyle(true) // Required for S3-compatible services
                .build();

        this.s3Presigner = S3Presigner.builder()
                .endpointOverride(java.net.URI.create(endpoint))
                .credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1)
                .build();
    }

    @Override
    public ImageResponse getImage(String imageId) throws IOException {
        try {
            // Get object metadata to verify it exists
            var headResponse = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .build());

            // Generate presigned URL (valid for 1 hour)
            String imageUrl = getPresignedImageUrl(imageId);

            // Extract extension from the ext parameter (remove the dot)
            String extension = imageId.substring(imageId.lastIndexOf("."));

            // Get the actual image bytes
            var getObjectResponse = s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .build());

            byte[] imageBytes = getObjectResponse.readAllBytes();

            return ImageResponse.builder()
                    .imageId(imageId) // Keep original imageId without extension
                    .extension(extension) // Extension without dot
                    .url(imageUrl) // Presigned URL
                    .bytes(imageBytes)
                    .build();

        } catch (NoSuchKeyException e) {
            throw new IOException("Image not found: " + imageId, e);
        } catch (S3Exception e) {
            throw new IOException("Failed to get image: " + imageId, e);
        }
    }

    public String getPresignedImageUrl(String imageId) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(1))
                    .getObjectRequest(getObjectRequest)
                    .build();

            return s3Presigner.presignGetObject(presignRequest).url().toString();

        } catch (Exception e) {
            throw new IOException("Failed to generate presigned URL for: " + imageId, e);
        }
    }

    @Override
    public List<ImageResponse> getImages(List<String> imageIds) throws IOException {
        List<ImageResponse> images = new ArrayList<>();
        List<String> notFound = new ArrayList<>();

        for (String imageId : imageIds) {
            try {
                images.add(getImage(imageId));
            } catch (IOException e) {
                notFound.add(imageId);
            }
        }

        if (!notFound.isEmpty()) {
            throw new IOException("Some images not found: " + notFound);
        }

        return images;
    }

    @Override
    public String storeImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Cannot store empty file");
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String imageId = generateUniqueFilename() + extension;

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            return imageId;

        } catch (S3Exception e) {
            throw new IOException("Failed to store image", e);
        }
    }

    @Override
    public boolean checkImageExists(String imageId) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            // Log the exception but return false for any other S3 errors
            return false;
        }
    }

    @Override
    public void deleteImage(String imageId) throws IOException {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageId)
                    .build());
        } catch (S3Exception e) {
            throw new IOException("Failed to delete image: " + imageId, e);
        }
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    // Helper method to close resources when the service is destroyed
    public void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}