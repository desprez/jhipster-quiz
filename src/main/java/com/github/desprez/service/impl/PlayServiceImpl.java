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

    private final Logger log = LoggerFactory.getLogger(QuizzServiceImpl.class);

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

        Attempt newAttempt = new Attempt().quizz(quizz).score(0).started(Instant.now().truncatedTo(ChronoUnit.SECONDS)); // Angular
        // don't
        // like
        // miliseconds

        log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin().get());
        String username = SecurityUtils.getCurrentUserLogin().get();
        newAttempt.setUser(userRepository.findOneByLogin(username).get());

        List<Question> shuffleMe = new ArrayList<Question>(quizz.getQuestions());
        if (quizz.getQuestionOrder() != null && quizz.getQuestionOrder().equals(DisplayOrder.RANDOM)) {
            Collections.shuffle(shuffleMe);
        }
        shuffleMe.forEach(question -> {
            AttemptAnswer attemptAnswer = new AttemptAnswer().attempt(newAttempt).question(question);
            newAttempt.getAnswers().add(attemptAnswer);
        });

        return attemptMapper.toDto(attemptRepository.save(newAttempt));
    }

    @Override
    public AttemptDTO evaluate(AttemptDTO attemptDTO) {
        Attempt attempt = attemptMapper.toEntity(attemptDTO);

        Quizz existingQuizz = quizzRepository.findOneWithQuestionRelationships(attempt.getQuizz().getId()).orElseThrow();

        int correctAnswercount = 0;
        int wrongAnswerCount = 0;
        int unansweredCount = 0;

        for (Question question : existingQuizz.getQuestions()) {
            boolean found = false;
            for (AttemptAnswer answer : attempt.getAnswers()) {
                if (answer.getQuestion().getId().equals(question.getId())) {
                    found = true;
                    Option optionAnswered = getOptionById(question, answer.getOption()).orElse(null);
                    answer.setOption(optionAnswered);
                    if (optionAnswered == null) {
                        unansweredCount++;
                        break;
                    }
                    if (checkIsCorrectAnswer(question, answer.getOption())) {
                        correctAnswercount++;
                        // answer.setCorrect();
                    } else {
                        wrongAnswerCount++;
                        // answer.setCorrect();
                    }
                    // answer.setCorrect()
                    break;
                }
            }
            if (!found) {
                unansweredCount++;
            }
        }
        attempt.setScore(correctAnswercount);
        log.info("correctAnswercount {}, wrongAnswerCount {}, unansweredCount {}", correctAnswercount, wrongAnswerCount, unansweredCount);
        // attempt.setCorrectAnswerCount(correctAnswercount);
        // attempt.setWrongAnswerCount(wrongAnswerCount);
        // attempt.setUnansweredCount(unansweredCount);
        attempt = attemptRepository.save(attempt);
        return attemptMapper.toDto(attempt);
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

    public Boolean checkIsCorrectAnswer(Question question, Option optionAnswer) {
        if (optionAnswer == null) {
            return false;
        }
        return question.getCorrectOptionIndex().equals(optionAnswer.getIndex());
    }
}
