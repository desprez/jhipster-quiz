package com.github.desprez.repository;

import com.github.desprez.domain.Question;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    @Query("select question from Question question left join fetch question.options where question.id =:id")
    Optional<Question> findOneWithOptionRelationships(@Param("id") UUID id);

    Integer countByQuizzId(UUID quizzId);

    List<Question> findByQuizzId(UUID quizzId);
}
