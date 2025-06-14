package danayaspace.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseUtil {

    private static final Map<String, String> EXTENSION_TO_MIME = new HashMap<>();

    static {
        EXTENSION_TO_MIME.put(".jpg", "image/jpeg");
        EXTENSION_TO_MIME.put(".jpeg", "image/jpeg");
        EXTENSION_TO_MIME.put(".png", "image/png");
        EXTENSION_TO_MIME.put(".gif", "image/gif");
        EXTENSION_TO_MIME.put(".bmp", "image/bmp");
        EXTENSION_TO_MIME.put(".webp", "image/webp");
        EXTENSION_TO_MIME.put(".heic", "image/heic");
        EXTENSION_TO_MIME.put(".heif", "image/heif");
    }

    public static ResponseEntity<ByteArrayResource> createImageResponse(byte[] imageBytes, String fileName, String extension) {
        String mimeType = EXTENSION_TO_MIME.getOrDefault(extension, "application/octet-stream");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));
        headers.setContentLength(imageBytes.length);
        headers.setContentDispositionFormData("inline", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(imageBytes));
    }
}
