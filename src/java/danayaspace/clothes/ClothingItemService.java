package danayaspace.clothes;

import org.springframework.stereotype.Service;

import danayaspace.api.ClothingItemResponse;
import danayaspace.api.CreateClothingItemRequest;
import danayaspace.file.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClothingItemService {

    private final FileStorageService fileStorageService;
    private final ClothingItemRepository clothingItemRepository;

    public ClothingItemResponse storeClothingItem(CreateClothingItemRequest request, Long userId) {
        if (fileStorageService.checkImageExists(request.getImageId())) {
            throw new IllegalArgumentException("Image with id " + request.getImageId() + " already exists");
        }
        ClothingItemEntity clothingItem = toEntity(request, userId);
        clothingItem = clothingItemRepository.save(clothingItem);

        return toResponse(clothingItem);
    }

    private ClothingItemEntity toEntity(CreateClothingItemRequest request, Long userId) {
        return ClothingItemEntity.builder()
                .name(request.getName())
                .userId(userId)
                .imageId(request.getImageId())
                .websiteName(request.getWebsiteName())
                .websiteUrl(request.getWebsiteUrl())
                .notes(request.getNotes())
                .favourite(request.isFavourite())
                .build();
    }

    private ClothingItemResponse toResponse(ClothingItemEntity clothingItem) {
        return ClothingItemResponse.builder()
                .id(clothingItem.getId())
                .websiteName(clothingItem.getWebsiteName())
                .websiteUrl(clothingItem.getWebsiteUrl())
                .notes(clothingItem.getNotes())
                .favourite(clothingItem.isFavourite())
                .createdOn(clothingItem.getCreatedOn())
                .build();
    }
}
