package com.github.desprez.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.repository.UserRepository;
import com.github.desprez.security.SecurityUtils;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.mapper.AttemptAnswerMapperImpl;
import com.github.desprez.service.mapper.AttemptMapper;
import com.github.desprez.service.mapper.AttemptMapperImpl;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class PlayServiceImplTest {

    private static final String CORRECT_OPTION_ID = "38aa6ead-d352-4616-a82b-85cbdbda6c9d";
    private static final String WRONG_OPTION_ID = "18aced7a-84ba-4179-a7af-c1e86ba069a2";
    private static final String QUESTION1_ID = "ea641920-1492-4cfd-b8ef-e0f58d5d9dc2";
    private static final String QUESTION2_ID = "598d3310-fa70-4409-89b9-c48b7601774f";
    private static final String QUESTION3_ID = "bcd7a793-9ca3-4a9d-b17d-3398b2eb0632";

    private PlayServiceImpl playService;
    private QuizzRepository quizzRepository;
    private UserRepository userRepository;
    private AttemptRepository attemptRepository;
    private AttemptMapper attemptMapper = new AttemptMapperImpl(new AttemptAnswerMapperImpl());

    @BeforeEach
    void setUp() {
        quizzRepository = mock(QuizzRepository.class);
        userRepository = mock(UserRepository.class);
        attemptRepository = mock(AttemptRepository.class);

        playService = new PlayServiceImpl(quizzRepository, userRepository, attemptMapper, attemptRepository);
    }

    @Test
    void testStart() {
        // Mocking dependencies
        Quizz quizz = fixtureWithQuizz();
        UUID quizId = quizz.getId();

        when(quizzRepository.findOneWithQuestionRelationships(quizId)).thenReturn(Optional.of(quizz));
        when(SecurityUtils.getCurrentUserLogin()).thenReturn(java.util.Optional.of("testUser"));
        when(userRepository.findOneByLogin("testUser")).thenReturn(java.util.Optional.of(new User()));

        // Mocking shuffled questions
        // List<Question> shuffledQuestions = new ArrayList<>();
        // Question question1 = new Question();
        // Question question2 = new Question();
        // shuffledQuestions.add(question1);
        // shuffledQuestions.add(question2);
        // when(quizz.getQuestions()).thenReturn(shuffledQuestions);

        // Mocking attempt and attempt DTO
        Attempt newAttempt = new Attempt();
        AttemptDTO expectedAttemptDTO = new AttemptDTO();
        when(attemptRepository.save(newAttempt)).thenReturn(newAttempt);

        // Call the method under test
        AttemptDTO actualAttemptDTO = playService.start(quizz.getId());

        // Verify the results
        assertThat(actualAttemptDTO).isEqualTo(expectedAttemptDTO);
    }

    @Test
    void testEvaluate() {
        // Mocking dependencies
        Quizz quizz = fixtureWithQuizz();

        Attempt attempt = fixtureWithAttempt(quizz);

        when(attemptRepository.save(any(Attempt.class)))
            .thenAnswer(
                new Answer<Attempt>() {
                    public Attempt answer(InvocationOnMock invocation) {
                        return (Attempt) invocation.getArguments()[0];
                    }
                }
            );

        // Call the method under test
        AttemptDTO actualAttemptDTO = playService.evaluate(attemptMapper.toDto(attempt));

        // Verify the results
        assertThat(actualAttemptDTO).isNotNull();
        assertThat(actualAttemptDTO.getScore()).isEqualTo(1);
        // assertThat(actualAttemptDTO.getCorrectAnswerCount()).isEqualTo(1);
        // assertThat(actualAttemptDTO.getWrongAnswerCount()).isEqualTo(1);
        // assertThat(actualAttemptDTO.getUnansweredCount()).isEqualTo(1);
    }

    private Attempt fixtureWithAttempt(Quizz quizz) {
        Attempt attempt = new Attempt();
        attempt.setQuizz(quizz);
        attempt.setAnswers(new HashSet<>());

        AttemptAnswer goodAnswer = new AttemptAnswer();
        goodAnswer.setId(UUID.randomUUID());
        goodAnswer.setQuestion(new Question().id(UUID.fromString(QUESTION2_ID)));
        goodAnswer.setOption(new Option().id(UUID.fromString(CORRECT_OPTION_ID)));
        attempt.getAnswers().add(goodAnswer);

        AttemptAnswer wrongAnswer = new AttemptAnswer();
        wrongAnswer.setId(UUID.randomUUID());
        wrongAnswer.setQuestion(new Question().id(UUID.fromString(QUESTION1_ID)));
        wrongAnswer.setOption(new Option().id(UUID.fromString(WRONG_OPTION_ID)));
        attempt.getAnswers().add(wrongAnswer);

        AttemptAnswer missingAnswer = new AttemptAnswer();
        missingAnswer.setId(UUID.randomUUID());
        missingAnswer.setQuestion(new Question().id(UUID.fromString(QUESTION3_ID)));
        missingAnswer.setOption(null);
        attempt.getAnswers().add(missingAnswer);

        return attempt;
    }

    private Quizz fixtureWithQuizz() {
        UUID quizId = UUID.randomUUID();
        Quizz quizz = new Quizz();
        quizz.setId(quizId);
        quizz.setQuestions(fixtureWithQuestions());
        when(quizzRepository.findOneWithQuestionRelationships(quizId)).thenReturn(java.util.Optional.of(quizz));
        return quizz;
    }

    private Set<Question> fixtureWithQuestions() {
        Option optionA = new Option().id(UUID.fromString("f9a845bd-3e13-48aa-a01c-2655789720df")).index(1).statement("A");
        Option optionB = new Option().id(UUID.fromString(WRONG_OPTION_ID)).index(2).statement("B");

        Question question1 = new Question().id(UUID.fromString(QUESTION1_ID)).correctOptionIndex(1).options(Set.of(optionA, optionB));

        Option optionC = new Option().id(UUID.fromString("bc1fd616-fa22-4de7-84b5-657414ad54ee")).index(1).statement("C");
        Option optionD = new Option().id(UUID.fromString(CORRECT_OPTION_ID)).index(2).statement("D");

        Question question2 = new Question().id(UUID.fromString(QUESTION2_ID)).correctOptionIndex(2).options(Set.of(optionC, optionD));

        Option optionE = new Option().id(UUID.fromString("0c872e54-f570-42eb-9e7e-c75f219d7d39")).index(1).statement("E");
        Option optionF = new Option().id(UUID.fromString("8a50db4f-603c-4247-b1e1-1e5532ffabb6")).index(2).statement("F");

        Question question3 = new Question().id(UUID.fromString(QUESTION3_ID)).correctOptionIndex(3).options(Set.of(optionE, optionF));

        return Set.of(question1, question2, question3);
    }
}
