package danayaspace.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.api.ImageResponse;
import danayaspace.exception.FileStorageException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorage fileStorage;

    public ImageResponse getImage(String imageId) {
        try {
            return fileStorage.getImage(imageId);
        } catch (IOException e) {
            return null;
        }
    }

    // Maps to base64 for response purposes
    public List<String> getImages() {
        try {
            List<ImageResponse> images = fileStorage.getImages(List.of("71c68db0-f16f-4da8-bd7e-fead046e19e3"));

            List<String> base64Images = new ArrayList<>();
            for (ImageResponse image : images) {
                base64Images.add(Base64.getEncoder().encodeToString(image.getBytes()));
            }
            return base64Images;
        } catch (IOException e) {
            return null;
        }
    }

    public String storeImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Empty file");
            }
            return fileStorage.storeImage(file);
        } catch (IOException e) {
            throw new FileStorageException("Storage failed: " + e.getMessage(), e);
        }
    }

    public boolean checkImageExists(String imageId) {
        return fileStorage.checkImageExists(imageId);
    }

    public void deleteImage(String imageId) {
        try {
            fileStorage.deleteImage(imageId);
        } catch (IOException e) {
            throw new FileStorageException("Delete failed: " + e.getMessage(), e);
        }
    }

    public String getBaseUrl() {
        return fileStorage.getBaseUrl();
    }
}
