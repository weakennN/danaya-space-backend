package danayaspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import danayaspace.config.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DanayaSpace {

    public static void main(String[] args) {
        SpringApplication.run(DanayaSpace.class, args);
    }
}
