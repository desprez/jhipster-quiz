package com.github.desprez.service.impl;

import com.github.desprez.domain.Question;
import com.github.desprez.repository.QuestionRepository;
import com.github.desprez.service.QuestionService;
import com.github.desprez.service.client.OpenDBRestClient;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.desprez.domain.Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final OpenDBRestClient openDBRestClient;

    public QuestionServiceImpl(QuestionRepository questionRepository, OpenDBRestClient openDBRestClient) {
        this.questionRepository = questionRepository;
        this.openDBRestClient = openDBRestClient;
    }

    @Override
    public Question save(Question question) {
        log.debug("Request to save Question : {}", question);
        return questionRepository.save(question);
    }

    @Override
    public Question update(Question question) {
        log.debug("Request to update Question : {}", question);
        return questionRepository.save(question);
    }

    @Override
    public Optional<Question> partialUpdate(Question question) {
        log.debug("Request to partially update Question : {}", question);

        return questionRepository
            .findById(question.getId())
            .map(existingQuestion -> {
                if (question.getStatement() != null) {
                    existingQuestion.setStatement(question.getStatement());
                }
                if (question.getIndex() != null) {
                    existingQuestion.setIndex(question.getIndex());
                }
                if (question.getCorrectOptionIndex() != null) {
                    existingQuestion.setCorrectOptionIndex(question.getCorrectOptionIndex());
                }

                return existingQuestion;
            })
            .map(questionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        log.debug("Request to get all Questions");
        return questionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findOne(UUID id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findOneWithOptionRelationships(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }

    @Override
    public Integer countByQuizzId(UUID quizzId) {
        return questionRepository.countByQuizzId(quizzId);
    }

    @Override
    public List<Question> suggest(Integer amount, Integer category, String difficulty) {
        return openDBRestClient.getQuizzFromOpenDB(amount, category, difficulty);
    }
}
