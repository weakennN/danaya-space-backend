package danayaspace.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.api.ImageResponse;
import danayaspace.file.FileStorageService;
import danayaspace.util.HttpResponseUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final FileStorageService fileStorageService;

    @GetMapping(value = "/{imageId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("imageId") String imageId) {
        ImageResponse image = fileStorageService.getImage(imageId);
        return HttpResponseUtil.createImageResponse(image.getBytes(), image.getImageId(), image.getExtension());
    }

    // TODO by user
    @GetMapping
    public ResponseEntity<List<String>> getImages() {
        return ResponseEntity.ok(fileStorageService.getImages());
    }

    @PostMapping
    public String saveImage(@RequestParam("image") MultipartFile image) {
        return fileStorageService.storeImage(image);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(@PathVariable("imageId") String imageId) {
        fileStorageService.deleteImage(imageId);
    }
}
