package com.github.desprez.domain;

import static com.github.desprez.domain.OptionTestSamples.*;
import static com.github.desprez.domain.QuestionTestSamples.*;
import static com.github.desprez.domain.QuizzTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void optionsTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        question.addOptions(optionBack);
        assertThat(question.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getQuestion()).isEqualTo(question);

        question.removeOptions(optionBack);
        assertThat(question.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getQuestion()).isNull();

        question.options(new HashSet<>(Set.of(optionBack)));
        assertThat(question.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getQuestion()).isEqualTo(question);

        question.setOptions(new HashSet<>());
        assertThat(question.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getQuestion()).isNull();
    }

    @Test
    void quizzTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Quizz quizzBack = getQuizzRandomSampleGenerator();

        question.setQuizz(quizzBack);
        assertThat(question.getQuizz()).isEqualTo(quizzBack);

        question.quizz(null);
        assertThat(question.getQuizz()).isNull();
    }
}
