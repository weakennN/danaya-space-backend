package danayaspace.clothes;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clothing_items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClothingItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "image_extension")
    private String imageExtension;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @Column(name = "website_name")
    private String websiteName;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "favourite")
    private boolean favourite;

    @Column(name = "created_on")
    @CreationTimestamp
    private OffsetDateTime createdOn;
}
