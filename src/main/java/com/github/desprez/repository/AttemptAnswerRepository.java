package com.github.desprez.repository;

import com.github.desprez.domain.AttemptAnswer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AttemptAnswer entity.
 */
@Repository
public interface AttemptAnswerRepository extends JpaRepository<AttemptAnswer, UUID> {
    default Optional<AttemptAnswer> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AttemptAnswer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AttemptAnswer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select attemptAnswer from AttemptAnswer attemptAnswer left join fetch attemptAnswer.question left join fetch attemptAnswer.option",
        countQuery = "select count(attemptAnswer) from AttemptAnswer attemptAnswer"
    )
    Page<AttemptAnswer> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select attemptAnswer from AttemptAnswer attemptAnswer left join fetch attemptAnswer.question left join fetch attemptAnswer.option"
    )
    List<AttemptAnswer> findAllWithToOneRelationships();

    @Query(
        "select attemptAnswer from AttemptAnswer attemptAnswer left join fetch attemptAnswer.question left join fetch attemptAnswer.option where attemptAnswer.id =:id"
    )
    Optional<AttemptAnswer> findOneWithToOneRelationships(@Param("id") UUID id);
}
