package com.github.desprez.service;

import com.github.desprez.domain.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.github.desprez.domain.Option}.
 */
public interface OptionService {
    /**
     * Save a option.
     *
     * @param option the entity to save.
     * @return the persisted entity.
     */
    Option save(Option option);

    /**
     * Updates a option.
     *
     * @param option the entity to update.
     * @return the persisted entity.
     */
    Option update(Option option);

    /**
     * Partially updates a option.
     *
     * @param option the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Option> partialUpdate(Option option);

    /**
     * Get all the options.
     *
     * @return the list of entities.
     */
    List<Option> findAll();

    /**
     * Get the "id" option.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Option> findOne(UUID id);

    /**
     * Delete the "id" option.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
