package com.github.desprez.service.impl;

import com.github.desprez.domain.Quizz;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.repository.UserRepository;
import com.github.desprez.service.QuizzServiceExtended;
import com.github.desprez.service.mapper.QuizzMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
@Transactional
public class QuizzServiceExtendedImpl extends QuizzServiceImpl implements QuizzServiceExtended {

    private final Logger log = LoggerFactory.getLogger(QuizzServiceExtendedImpl.class);

    public QuizzServiceExtendedImpl(QuizzRepository quizzRepository, QuizzMapper quizzMapper, UserRepository userRepository) {
        super(quizzRepository, quizzMapper, userRepository);
    }

    @Override
    public void publishQuiz(UUID id) {
        log.debug("Request to publish Quizz : {}", id);

        Quizz existingQuizz = quizzRepository.findOneWithQuestionRelationships(id).orElseThrow();
        if (Boolean.TRUE.equals(existingQuizz.getPublished())) {
            throw new IllegalStateException("The quiz is already published");
        }
        if (existingQuizz.getQuestions() == null || existingQuizz.getQuestions().isEmpty() || existingQuizz.getQuestions().size() < 2) {
            throw new IllegalStateException("Minimum 2 questions are required to publish the quiz.");
        }

        existingQuizz.setPublished(true);
        quizzRepository.save(existingQuizz);
    }
}
