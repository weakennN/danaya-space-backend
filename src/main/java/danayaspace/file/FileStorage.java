package danayaspace.file;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import danayaspace.api.ImageResponse;

public interface FileStorage {

    ImageResponse getImage(String imageId) throws IOException;

    List<ImageResponse> getImages(List<String> imageIds) throws IOException;

    String storeImage(MultipartFile file) throws IOException;

    boolean checkImageExists(String imageId);

    void deleteImage(String imageId) throws IOException;

    String getBaseUrl();

    default String generateUniqueFilename() {
        return UUID.randomUUID().toString();
    }
}
