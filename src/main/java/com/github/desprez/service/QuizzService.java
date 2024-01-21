package com.github.desprez.service;

import com.github.desprez.service.dto.QuizzDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.desprez.domain.Quizz}.
 */
public interface QuizzService {
    /**
     * Save a quizz.
     *
     * @param quizzDTO the entity to save.
     * @return the persisted entity.
     */
    QuizzDTO save(QuizzDTO quizzDTO);

    /**
     * Updates a quizz.
     *
     * @param quizzDTO the entity to update.
     * @return the persisted entity.
     */
    QuizzDTO update(QuizzDTO quizzDTO);

    /**
     * Partially updates a quizz.
     *
     * @param quizzDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuizzDTO> partialUpdate(QuizzDTO quizzDTO);

    /**
     * Get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuizzDTO> findAll(Pageable pageable);

    /**
     * Get all the quizzes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuizzDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" quizz.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuizzDTO> findOne(UUID id);

    /**
     * Delete the "id" quizz.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
