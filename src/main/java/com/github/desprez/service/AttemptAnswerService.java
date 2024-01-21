package com.github.desprez.service;

import com.github.desprez.domain.AttemptAnswer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.desprez.domain.AttemptAnswer}.
 */
public interface AttemptAnswerService {
    /**
     * Save a attemptAnswer.
     *
     * @param attemptAnswer the entity to save.
     * @return the persisted entity.
     */
    AttemptAnswer save(AttemptAnswer attemptAnswer);

    /**
     * Updates a attemptAnswer.
     *
     * @param attemptAnswer the entity to update.
     * @return the persisted entity.
     */
    AttemptAnswer update(AttemptAnswer attemptAnswer);

    /**
     * Partially updates a attemptAnswer.
     *
     * @param attemptAnswer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttemptAnswer> partialUpdate(AttemptAnswer attemptAnswer);

    /**
     * Get all the attemptAnswers.
     *
     * @return the list of entities.
     */
    List<AttemptAnswer> findAll();

    /**
     * Get all the attemptAnswers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttemptAnswer> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" attemptAnswer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttemptAnswer> findOne(UUID id);

    /**
     * Delete the "id" attemptAnswer.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
