package com.github.desprez.service;

import com.github.desprez.domain.*; // for static metamodels
import com.github.desprez.domain.Quizz;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.service.criteria.QuizzCriteria;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.mapper.QuizzMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Quizz} entities in the database.
 * The main input is a {@link QuizzCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuizzDTO} or a {@link Page} of {@link QuizzDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuizzQueryService extends QueryService<Quizz> {

    private final Logger log = LoggerFactory.getLogger(QuizzQueryService.class);

    private final QuizzRepository quizzRepository;

    private final QuizzMapper quizzMapper;

    public QuizzQueryService(QuizzRepository quizzRepository, QuizzMapper quizzMapper) {
        this.quizzRepository = quizzRepository;
        this.quizzMapper = quizzMapper;
    }

    /**
     * Return a {@link List} of {@link QuizzDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuizzDTO> findByCriteria(QuizzCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Quizz> specification = createSpecification(criteria);
        return quizzMapper.toDto(quizzRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuizzDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuizzDTO> findByCriteria(QuizzCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quizz> specification = createSpecification(criteria);
        return quizzRepository.findAll(specification, page).map(quizzMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuizzCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Quizz> specification = createSpecification(criteria);
        return quizzRepository.count(specification);
    }

    /**
     * Function to convert {@link QuizzCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quizz> createSpecification(QuizzCriteria criteria) {
        Specification<Quizz> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Quizz_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Quizz_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Quizz_.description));
            }
            if (criteria.getDifficulty() != null) {
                specification = specification.and(buildSpecification(criteria.getDifficulty(), Quizz_.difficulty));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getCategory(), Quizz_.category));
            }
            if (criteria.getQuestionOrder() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionOrder(), Quizz_.questionOrder));
            }
            if (criteria.getMaxAnswerTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaxAnswerTime(), Quizz_.maxAnswerTime));
            }
            if (criteria.getAllowBack() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowBack(), Quizz_.allowBack));
            }
            if (criteria.getAllowReview() != null) {
                specification = specification.and(buildSpecification(criteria.getAllowReview(), Quizz_.allowReview));
            }
            if (criteria.getSecretGoodAnwers() != null) {
                specification = specification.and(buildSpecification(criteria.getSecretGoodAnwers(), Quizz_.secretGoodAnwers));
            }
            if (criteria.getPublished() != null) {
                specification = specification.and(buildSpecification(criteria.getPublished(), Quizz_.published));
            }
            if (criteria.getQuestionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getQuestionsId(), root -> root.join(Quizz_.questions, JoinType.LEFT).get(Question_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Quizz_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
