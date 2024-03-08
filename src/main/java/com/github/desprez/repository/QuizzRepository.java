package com.github.desprez.repository;

import com.github.desprez.domain.Quizz;
import com.github.desprez.service.dto.QuizzDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quizz entity.
 */
@Repository
public interface QuizzRepository extends JpaRepository<Quizz, UUID>, JpaSpecificationExecutor<Quizz> {
    @Query("select quizz from Quizz quizz where quizz.user.login = ?#{authentication.name}")
    List<Quizz> findByUserIsCurrentUser();

    default Optional<Quizz> findOneWithEagerRelationships(UUID id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Quizz> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Quizz> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select quizz from Quizz quizz left join fetch quizz.user", countQuery = "select count(quizz) from Quizz quizz")
    Page<Quizz> findAllWithToOneRelationships(Pageable pageable);

    @Query("select quizz from Quizz quizz left join fetch quizz.user")
    List<Quizz> findAllWithToOneRelationships();

    @Query("select quizz from Quizz quizz left join fetch quizz.user where quizz.id =:id")
    Optional<Quizz> findOneWithToOneRelationships(@Param("id") UUID id);

    @Query("select quizz from Quizz quizz left join fetch quizz.user left join fetch quizz.questions questions where quizz.id =:id")
    Optional<Quizz> findOneWithQuestionRelationships(@Param("id") UUID id);

    @Query("select quizz from Quizz quizz where quizz.user.login = ?#{principal.username}")
    Page<QuizzDTO> findByUserIsCurrentUser(Pageable pageable);
}
