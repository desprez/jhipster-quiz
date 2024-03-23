package com.github.desprez.service.impl;

import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.enumeration.DisplayOrder;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.repository.UserRepository;
import com.github.desprez.security.SecurityUtils;
import com.github.desprez.service.PlayService;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.mapper.AttemptMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing the Quizz playground .
 */
@Service
@Transactional
public class PlayServiceImpl implements PlayService {

    private final Logger log = LoggerFactory.getLogger(PlayServiceImpl.class);

    private final QuizzRepository quizzRepository;

    private final UserRepository userRepository;

    private AttemptMapper attemptMapper;

    private AttemptRepository attemptRepository;

    public PlayServiceImpl(
        QuizzRepository quizzRepository,
        UserRepository userRepository,
        AttemptMapper attemptMapper,
        AttemptRepository attemptRepository
    ) {
        this.quizzRepository = quizzRepository;
        this.userRepository = userRepository;
        this.attemptMapper = attemptMapper;
        this.attemptRepository = attemptRepository;
    }

    @Override
    public AttemptDTO start(UUID quizId) {
        log.debug("Request to play Quizz : {}", quizId);

        Quizz quizz = quizzRepository.findOneWithQuestionRelationships(quizId).orElseThrow();

        Attempt newAttempt = new Attempt()
            .quizz(quizz)
            .correctAnswerCount(0)
            .wrongAnswerCount(0)
            .unansweredCount(quizz.getQuestionCount())
            .started(Instant.now().truncatedTo(ChronoUnit.SECONDS)); // No more accuracy needed

        String username = getUsername();
        newAttempt.setUser(userRepository.findOneByLogin(username).orElseThrow());

        List<Question> shuffleMe = new ArrayList<>(quizz.getQuestions());
        if (DisplayOrder.RANDOM.equals(quizz.getQuestionOrder())) {
            Collections.shuffle(shuffleMe);
        }
        shuffleMe.forEach(question -> {
            AttemptAnswer attemptAnswer = new AttemptAnswer().attempt(newAttempt).question(question);
            newAttempt.getAnswers().add(attemptAnswer);
        });

        return attemptMapper.toDto(attemptRepository.save(newAttempt));
    }

    private String getUsername() {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
        log.debug("No user passed in, using current user: {}", username);
        return username;
    }

    @Override
    public AttemptDTO evaluate(AttemptDTO attemptDTO) {
        Attempt attempt = attemptMapper.toEntity(attemptDTO);
        attempt.correctAnswerCount(0).wrongAnswerCount(0).unansweredCount(0);
        Quizz existingQuizz = quizzRepository.findOneWithQuestionRelationships(attempt.getQuizz().getId()).orElseThrow();

        for (Question question : existingQuizz.getQuestions()) {
            evaluateQuestion(attempt, question);
        }
        attempt.ended(Instant.now().truncatedTo(ChronoUnit.SECONDS));

        attempt = attemptRepository.save(attempt);
        log.info("attempt evaluated {}", attempt);
        return attemptMapper.toDto(attempt);
    }

    private void evaluateQuestion(Attempt attempt, Question question) {
        boolean found = false;
        for (AttemptAnswer answer : attempt.getAnswers()) {
            if (answer.getQuestion().getId().equals(question.getId())) {
                found = true;
                Option optionAnswered = getOptionById(question, answer.getOption()).orElse(null);
                answer.setOption(optionAnswered);
                if (optionAnswered == null) {
                    attempt.incrementUnansweredCount();
                    break;
                }
                if (checkIsCorrectAnswer(question, answer.getOption())) {
                    attempt.incrementCorrectAnswerCount();
                    answer.correct(true);
                } else {
                    attempt.incrementWrongAnswerCount();
                    answer.correct(false);
                }
            }
        }
        if (!found) {
            attempt.incrementUnansweredCount();
        }
    }

    private Optional<Option> getOptionById(Question question, Option optionAnswered) {
        if (optionAnswered != null && optionAnswered.getId() != null) {
            for (Option option : question.getOptions()) {
                if (option.getId().equals(optionAnswered.getId())) {
                    return Optional.of(option);
                }
            }
        }
        return Optional.empty();
    }

    public boolean checkIsCorrectAnswer(Question question, Option optionAnswer) {
        return question.getCorrectOptionIndex().equals(optionAnswer.getIndex());
    }
}
