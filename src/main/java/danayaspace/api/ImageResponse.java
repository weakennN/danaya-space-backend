package danayaspace.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImageResponse {

    private String imageId;
    private String extension;
    private String url;
    private byte[] bytes;
}
