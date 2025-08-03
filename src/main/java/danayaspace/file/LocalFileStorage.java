package danayaspace.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.api.ImageResponse;
import danayaspace.config.StorageProperties;
import danayaspace.exception.FileStorageException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final StorageProperties storageProperties;

    @Override
    public ImageResponse getImage(String imageId) throws IOException {
        Path rootLocation = getRoot();
        return getImageFromRoot(imageId, rootLocation);
    }

    @Override
    public List<ImageResponse> getImages(List<String> imageIds) throws IOException {
        Path rootLocation = getRoot();

        List<ImageResponse> result = new ArrayList<>();
        for (String imageId : imageIds) {
            ImageResponse image = getImageFromRoot(imageId, rootLocation);
            if (image != null) {
                result.add(image);
            }
        }

        return result;
    }

    @Override
    public String storeImage(MultipartFile file) throws IOException {
        if (file.getSize() > storageProperties.getMaxFileSize()) {
            throw new FileStorageException("File size exceeds limit of " + storageProperties.getMaxFileSize() + " bytes");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new FileStorageException("Invalid file type: " + file.getContentType());
        }

        String extension = getExtensionFromContentType(file.getContentType());
        String filename = generateUniqueFilename() + extension;

        Path rootLocation = getRoot();
        Path destination = rootLocation.resolve(filename).normalize().toAbsolutePath();

        if (!destination.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new FileStorageException("Invalid file path");
        }

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        return filename;
    }

    @Override
    public boolean checkImageExists(String imageId) {
        Path rootLocation = getRoot();

        Path candidate = rootLocation.resolve(imageId).normalize();

        if (!candidate.getParent().equals(rootLocation)) {
            throw new FileStorageException("Invalid image path");
        }

        return Files.exists(candidate) && Files.isRegularFile(candidate);
    }

    @Override
    public void deleteImage(String imageId) throws IOException {
        Path rootLocation = getRoot();

        ImageResponse image = getImageFromRoot(imageId, rootLocation);
        if (image == null) {
            return;
        }

        Files.delete(rootLocation.resolve(image.getImageId()));
    }

    @Override
    public String getBaseUrl() {
        return "/uploads/";
    }



    private String getExtensionFromContentType(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            case "image/gif":
                return ".gif";
            case "image/webp":
                return ".webp";
            case "image/bmp":
                return ".bmp";
            case "image/heic":
            case "image/heif":
                return ".heic"; // or ".heif" depending on your storage preference
            default:
                throw new FileStorageException("Unsupported image type: " + contentType);
        }
    }

    private ImageResponse getImageFromRoot(String imageId, Path rootLocation) throws IOException {
        Path candidate = rootLocation.resolve(imageId).normalize();

        if (Files.exists(candidate)) {
            byte[] bytes = Files.readAllBytes(candidate);

            return ImageResponse.builder()
                    .imageId(imageId)
                    .extension(imageId.substring(imageId.lastIndexOf(".")))
                    .bytes(bytes)
                    .build();
        }

        return null;
    }

    private Path getRoot() {
        return Paths.get(storageProperties.getLocation()).toAbsolutePath();
    }
}