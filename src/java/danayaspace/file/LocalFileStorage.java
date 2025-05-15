package danayaspace.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.config.StorageProperties;
import danayaspace.exception.FileStorageException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final StorageProperties storageProperties;

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

        Path rootLocation = Paths.get(storageProperties.getLocation());
        Path destination = rootLocation.resolve(filename).normalize().toAbsolutePath();

        if (!destination.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new FileStorageException("Invalid file path");
        }

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        }
        return filename.replace(extension, "");
    }

    @Override
    public boolean checkImageExists(String imageId) {
        Path rootLocation = Paths.get(storageProperties.getLocation()).toAbsolutePath();

        boolean exists = false;
        for (String ext : storageProperties.getSupportedExtensions()) {
            Path candidate = rootLocation.resolve(imageId + ext).normalize();

            if (!candidate.getParent().equals(rootLocation)) {
                throw new FileStorageException("Invalid image path");
            }

            if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
                exists = true;
                break;
            }
        }

        return exists;
    }

    private String generateUniqueFilename() {
        return UUID.randomUUID().toString();
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
}