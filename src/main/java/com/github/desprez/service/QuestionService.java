package com.github.desprez.service;

import com.github.desprez.domain.Question;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link com.github.desprez.domain.Question}.
 */
public interface QuestionService {
    /**
     * Save a question.
     *
     * @param question the entity to save.
     * @return the persisted entity.
     */
    Question save(Question question);

    /**
     * Updates a question.
     *
     * @param question the entity to update.
     * @return the persisted entity.
     */
    Question update(Question question);

    /**
     * Partially updates a question.
     *
     * @param question the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Question> partialUpdate(Question question);

    /**
     * Get all the questions.
     *
     * @return the list of entities.
     */
    List<Question> findAll();

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Question> findOne(UUID id);

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     *
     * @param id the id of the entity.
     * @return
     */
    Integer countByQuizzId(UUID id);

    /**
     * Find Questions from OpenDB according to the parameters
     * @param amount
     * @param category
     * @param difficulty
     * @return List of Questions
     */
    List<Question> suggest(Integer amount, Integer category, String difficulty);
}
