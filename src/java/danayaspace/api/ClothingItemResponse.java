package danayaspace.api;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClothingItemResponse {

    private Long id;
    private String websiteName;
    private String websiteUrl;
    private String notes;
    private boolean favourite;
    private OffsetDateTime createdOn;
}
