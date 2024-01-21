package com.github.desprez.domain;

import static com.github.desprez.domain.AttemptAnswerTestSamples.*;
import static com.github.desprez.domain.AttemptTestSamples.*;
import static com.github.desprez.domain.QuizzTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AttemptTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attempt.class);
        Attempt attempt1 = getAttemptSample1();
        Attempt attempt2 = new Attempt();
        assertThat(attempt1).isNotEqualTo(attempt2);

        attempt2.setId(attempt1.getId());
        assertThat(attempt1).isEqualTo(attempt2);

        attempt2 = getAttemptSample2();
        assertThat(attempt1).isNotEqualTo(attempt2);
    }

    @Test
    void answersTest() throws Exception {
        Attempt attempt = getAttemptRandomSampleGenerator();
        AttemptAnswer attemptAnswerBack = getAttemptAnswerRandomSampleGenerator();

        attempt.addAnswers(attemptAnswerBack);
        assertThat(attempt.getAnswers()).containsOnly(attemptAnswerBack);
        assertThat(attemptAnswerBack.getAttempt()).isEqualTo(attempt);

        attempt.removeAnswers(attemptAnswerBack);
        assertThat(attempt.getAnswers()).doesNotContain(attemptAnswerBack);
        assertThat(attemptAnswerBack.getAttempt()).isNull();

        attempt.answers(new HashSet<>(Set.of(attemptAnswerBack)));
        assertThat(attempt.getAnswers()).containsOnly(attemptAnswerBack);
        assertThat(attemptAnswerBack.getAttempt()).isEqualTo(attempt);

        attempt.setAnswers(new HashSet<>());
        assertThat(attempt.getAnswers()).doesNotContain(attemptAnswerBack);
        assertThat(attemptAnswerBack.getAttempt()).isNull();
    }

    @Test
    void quizzTest() throws Exception {
        Attempt attempt = getAttemptRandomSampleGenerator();
        Quizz quizzBack = getQuizzRandomSampleGenerator();

        attempt.setQuizz(quizzBack);
        assertThat(attempt.getQuizz()).isEqualTo(quizzBack);

        attempt.quizz(null);
        assertThat(attempt.getQuizz()).isNull();
    }
}
