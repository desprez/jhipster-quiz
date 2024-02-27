package com.github.desprez.service;

import com.github.desprez.service.dto.AttemptBasicDTO;
import com.github.desprez.service.dto.AttemptDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.desprez.domain.Attempt}.
 */
public interface AttemptService {
    /**
     * Save a attempt.
     *
     * @param attemptDTO the entity to save.
     * @return the persisted entity.
     */
    AttemptDTO save(AttemptDTO attemptDTO);

    /**
     * Updates a attempt.
     *
     * @param attemptDTO the entity to update.
     * @return the persisted entity.
     */
    AttemptDTO update(AttemptDTO attemptDTO);

    /**
     * Partially updates a attempt.
     *
     * @param attemptDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttemptDTO> partialUpdate(AttemptDTO attemptDTO);

    /**
     * Get all the attempts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttemptBasicDTO> findAll(Pageable pageable);

    /**
     * Get all the attempts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AttemptBasicDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" attempt.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttemptDTO> findOne(UUID id);

    /**
     * Delete the "id" attempt.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
