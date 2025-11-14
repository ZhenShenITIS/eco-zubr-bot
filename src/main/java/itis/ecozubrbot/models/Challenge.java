package itis.ecozubrbot.models;

import itis.ecozubrbot.constants.StringConstants;
import jakarta.persistence.*;
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
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private Integer experienceReward;
    private Integer pointsReward;
    private String imageUrl;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserChallenge> userChallenges = new ArrayList<>();

    @Override
    public String toString() {
        return StringConstants.TASK_DETAILS_TEMPLATE.getValue().formatted(title, description, pointsReward, experienceReward);
    }
}
