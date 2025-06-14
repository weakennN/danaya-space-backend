package danayaspace.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import danayaspace.api.ClothingItemResponse;
import danayaspace.api.CreateClothingItemRequest;
import danayaspace.clothes.ClothingItemService;
import danayaspace.security.SecurityUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clothing")
public class ClothingItemController {

    private final ClothingItemService clothingItemService;

    @PostMapping("/items")
    public ClothingItemResponse createItem(@RequestBody CreateClothingItemRequest request) {
        return clothingItemService.storeClothingItem(request, SecurityUtils.getUserId());
    }

    @GetMapping("/items")
    public List<ClothingItemResponse> getAllItems() {
        return clothingItemService.getClothingItems(SecurityUtils.getUserId());
    }
}
