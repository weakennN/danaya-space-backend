package danayaspace.clothes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import danayaspace.api.ClothingItemResponse;
import danayaspace.api.CreateClothingItemRequest;
import danayaspace.api.ImageResponse;
import danayaspace.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClothingItemService {

    private final FileStorageService fileStorageService;
    private final ClothingItemRepository clothingItemRepository;

    public List<ClothingItemResponse> getClothingItems(Long userId) {
        return clothingItemRepository.findByUserId(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ClothingItemResponse storeClothingItem(CreateClothingItemRequest request, Long userId) {
        ImageResponse image = fileStorageService.getImage(request.getImageId());
        if (image == null) {
            throw new IllegalArgumentException("Image with id " + request.getImageId() + " does not exist");
        }
        ClothingItemEntity clothingItem = toEntity(request, userId, image);
        clothingItem = clothingItemRepository.save(clothingItem);

        return toResponse(clothingItem);
    }

    private ClothingItemEntity toEntity(CreateClothingItemRequest request, Long userId, ImageResponse image) {
        return ClothingItemEntity.builder()
                .name(request.getName())
                .userId(userId)
                .imageId(image.getImageId())
                .imageExtension(image.getExtension())
                .websiteName(request.getWebsiteName())
                .websiteUrl(request.getWebsiteUrl())
                .notes(request.getNotes())
                .favourite(request.isFavourite())
                .build();
    }

    private ClothingItemResponse toResponse(ClothingItemEntity clothingItem) {
        return ClothingItemResponse.builder()
                .id(clothingItem.getId())
                .imageLink(fileStorageService.getBaseUrl() + clothingItem.getImageId() + clothingItem.getImageExtension())
                .websiteName(clothingItem.getWebsiteName())
                .websiteUrl(clothingItem.getWebsiteUrl())
                .notes(clothingItem.getNotes())
                .favourite(clothingItem.isFavourite())
                .createdOn(clothingItem.getCreatedOn())
                .build();
    }
}
