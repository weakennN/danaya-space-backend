package danayaspace.file;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.exception.FileStorageException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorage fileStorage;

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
}
