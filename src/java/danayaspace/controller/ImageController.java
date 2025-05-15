package danayaspace.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import danayaspace.file.FileStorageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public String saveImage(@RequestParam("image") MultipartFile image) {
       return fileStorageService.storeImage(image);
    }
}
