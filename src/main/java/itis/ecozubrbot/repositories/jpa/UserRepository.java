package itis.ecozubrbot.repositories.jpa;

import itis.ecozubrbot.models.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(
            """
           select u
           from User u
           join u.pet p
           order by p.experience desc, u.id asc
           """)
    List<User> findTopByPetExperienceDesc(Pageable pageable);

    @Query(
            value =
                    """
     with ranked as (
       select u.id,
              rank() over (order by p.experience desc, u.id) as rnk
       from bot_user u join pet p on p.id = u.id
     )
     select rnk from ranked where id = :userId
     """,
            nativeQuery = true)
    Integer findRankByUserId(@Param("userId") Long userId);
}
