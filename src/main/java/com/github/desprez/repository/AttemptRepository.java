package com.github.desprez.repository;

import com.github.desprez.domain.Attempt;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attempt entity.
 */
@Repository
public interface AttemptRepository extends JpaRepository<Attempt, UUID> {
    @Query("select attempt from Attempt attempt where attempt.user.login = ?#{authentication.name}")
    List<Attempt> findByUserIsCurrentUser();

    default Optional<Attempt> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Attempt> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Attempt> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select attempt from Attempt attempt left join fetch attempt.quizz left join fetch attempt.user",
        countQuery = "select count(attempt) from Attempt attempt"
    )
    Page<Attempt> findAllWithToOneRelationships(Pageable pageable);

    @Query("select attempt from Attempt attempt left join fetch attempt.quizz left join fetch attempt.user")
    List<Attempt> findAllWithToOneRelationships();

    @Query("select attempt from Attempt attempt left join fetch attempt.quizz left join fetch attempt.user where attempt.id =:id")
    Optional<Attempt> findOneWithToOneRelationships(@Param("id") UUID id);
}
