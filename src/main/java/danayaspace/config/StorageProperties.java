package danayaspace.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties("storage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageProperties {
    private String location;
    private long maxFileSize;
    private List<String> supportedExtensions;
}
