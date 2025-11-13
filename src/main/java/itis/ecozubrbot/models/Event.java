package itis.ecozubrbot.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private String city;
    private LocalDateTime endDateTime;
    private Integer experienceReward;
    private Integer pointsReward;
    private String imageUrl;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserEvent> userEvents = new ArrayList<>();

    @Override
    public String toString() {
        return title + "\n\n" + description + "\n\nНаграда в очках и опыте:\n" + pointsReward + "/" + experienceReward;
    }
}
