package com.github.desprez.domain;

import static com.github.desprez.domain.QuestionTestSamples.*;
import static com.github.desprez.domain.QuizzTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuizzTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quizz.class);
        Quizz quizz1 = getQuizzSample1();
        Quizz quizz2 = new Quizz();
        assertThat(quizz1).isNotEqualTo(quizz2);

        quizz2.setId(quizz1.getId());
        assertThat(quizz1).isEqualTo(quizz2);

        quizz2 = getQuizzSample2();
        assertThat(quizz1).isNotEqualTo(quizz2);
    }

    @Test
    void questionsTest() throws Exception {
        Quizz quizz = getQuizzRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        quizz.addQuestions(questionBack);
        assertThat(quizz.getQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getQuizz()).isEqualTo(quizz);

        quizz.removeQuestions(questionBack);
        assertThat(quizz.getQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getQuizz()).isNull();

        quizz.questions(new HashSet<>(Set.of(questionBack)));
        assertThat(quizz.getQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getQuizz()).isEqualTo(quizz);

        quizz.setQuestions(new HashSet<>());
        assertThat(quizz.getQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getQuizz()).isNull();
    }
}
