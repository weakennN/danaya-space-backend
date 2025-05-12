package danayaspace.clothes;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clothing_item")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClothingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "image_id")
    private Long imageId;

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
    private OffsetDateTime createdOn;
}
