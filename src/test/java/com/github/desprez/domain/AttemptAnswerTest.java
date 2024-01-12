package com.github.desprez.domain;

import static com.github.desprez.domain.AttemptAnswerTestSamples.*;
import static com.github.desprez.domain.AttemptTestSamples.*;
import static com.github.desprez.domain.OptionTestSamples.*;
import static com.github.desprez.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttemptAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttemptAnswer.class);
        AttemptAnswer attemptAnswer1 = getAttemptAnswerSample1();
        AttemptAnswer attemptAnswer2 = new AttemptAnswer();
        assertThat(attemptAnswer1).isNotEqualTo(attemptAnswer2);

        attemptAnswer2.setId(attemptAnswer1.getId());
        assertThat(attemptAnswer1).isEqualTo(attemptAnswer2);

        attemptAnswer2 = getAttemptAnswerSample2();
        assertThat(attemptAnswer1).isNotEqualTo(attemptAnswer2);
    }

    @Test
    void questionTest() throws Exception {
        AttemptAnswer attemptAnswer = getAttemptAnswerRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        attemptAnswer.setQuestion(questionBack);
        assertThat(attemptAnswer.getQuestion()).isEqualTo(questionBack);

        attemptAnswer.question(null);
        assertThat(attemptAnswer.getQuestion()).isNull();
    }

    @Test
    void optionTest() throws Exception {
        AttemptAnswer attemptAnswer = getAttemptAnswerRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        attemptAnswer.setOption(optionBack);
        assertThat(attemptAnswer.getOption()).isEqualTo(optionBack);

        attemptAnswer.option(null);
        assertThat(attemptAnswer.getOption()).isNull();
    }

    @Test
    void attemptTest() throws Exception {
        AttemptAnswer attemptAnswer = getAttemptAnswerRandomSampleGenerator();
        Attempt attemptBack = getAttemptRandomSampleGenerator();

        attemptAnswer.setAttempt(attemptBack);
        assertThat(attemptAnswer.getAttempt()).isEqualTo(attemptBack);

        attemptAnswer.attempt(null);
        assertThat(attemptAnswer.getAttempt()).isNull();
    }
}
