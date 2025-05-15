package danayaspace.file;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {

    String storeImage(MultipartFile file) throws IOException;

    boolean checkImageExists(String ImageId);
}
