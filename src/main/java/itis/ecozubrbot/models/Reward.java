package itis.ecozubrbot.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "text")
    private String description;
    private Integer pointsCost;
    private String value;
    private Integer availableQuantity;
    private String imageUrl;

    @Override
    public String toString() {
        return title + "\n\n" + description + "\n\nCтоимость/наличие:\n" + pointsCost + "/" + availableQuantity;
    }
}
