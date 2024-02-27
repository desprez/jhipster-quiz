package com.github.desprez.service.impl;

import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.repository.AttemptAnswerRepository;
import com.github.desprez.service.AttemptAnswerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.desprez.domain.AttemptAnswer}.
 */
@Service
@Transactional
public class AttemptAnswerServiceImpl implements AttemptAnswerService {

    private final Logger log = LoggerFactory.getLogger(AttemptAnswerServiceImpl.class);

    private final AttemptAnswerRepository attemptAnswerRepository;

    public AttemptAnswerServiceImpl(AttemptAnswerRepository attemptAnswerRepository) {
        this.attemptAnswerRepository = attemptAnswerRepository;
    }

    @Override
    public AttemptAnswer save(AttemptAnswer attemptAnswer) {
        log.debug("Request to save AttemptAnswer : {}", attemptAnswer);
        return attemptAnswerRepository.save(attemptAnswer);
    }

    @Override
    public AttemptAnswer update(AttemptAnswer attemptAnswer) {
        log.debug("Request to update AttemptAnswer : {}", attemptAnswer);
        return attemptAnswerRepository.save(attemptAnswer);
    }

    @Override
    public Optional<AttemptAnswer> partialUpdate(AttemptAnswer attemptAnswer) {
        log.debug("Request to partially update AttemptAnswer : {}", attemptAnswer);

        return attemptAnswerRepository
            .findById(attemptAnswer.getId())
            .map(existingAttemptAnswer -> {
                if (attemptAnswer.getStarted() != null) {
                    existingAttemptAnswer.setStarted(attemptAnswer.getStarted());
                }
                if (attemptAnswer.getEnded() != null) {
                    existingAttemptAnswer.setEnded(attemptAnswer.getEnded());
                }
                if (attemptAnswer.getCorrect() != null) {
                    existingAttemptAnswer.setCorrect(attemptAnswer.getCorrect());
                }

                return existingAttemptAnswer;
            })
            .map(attemptAnswerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptAnswer> findAll() {
        log.debug("Request to get all AttemptAnswers");
        return attemptAnswerRepository.findAll();
    }

    public Page<AttemptAnswer> findAllWithEagerRelationships(Pageable pageable) {
        return attemptAnswerRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttemptAnswer> findOne(UUID id) {
        log.debug("Request to get AttemptAnswer : {}", id);
        return attemptAnswerRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete AttemptAnswer : {}", id);
        attemptAnswerRepository.deleteById(id);
    }
}
