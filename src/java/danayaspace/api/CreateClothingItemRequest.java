package danayaspace.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateClothingItemRequest {

    private String name;
    private String websiteName;
    private String websiteUrl;
    private String notes;
    private boolean favourite;
}
