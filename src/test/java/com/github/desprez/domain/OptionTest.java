package com.github.desprez.domain;

import static com.github.desprez.domain.OptionTestSamples.*;
import static com.github.desprez.domain.QuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = getOptionSample1();
        Option option2 = new Option();
        assertThat(option1).isNotEqualTo(option2);

        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);

        option2 = getOptionSample2();
        assertThat(option1).isNotEqualTo(option2);
    }

    @Test
    void questionTest() throws Exception {
        Option option = getOptionRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        option.setQuestion(questionBack);
        assertThat(option.getQuestion()).isEqualTo(questionBack);

        option.question(null);
        assertThat(option.getQuestion()).isNull();
    }
}
