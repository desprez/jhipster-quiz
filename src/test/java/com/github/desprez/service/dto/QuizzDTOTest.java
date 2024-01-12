package com.github.desprez.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class QuizzDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzDTO.class);
        QuizzDTO quizzDTO1 = new QuizzDTO();
        quizzDTO1.setId(UUID.randomUUID());
        QuizzDTO quizzDTO2 = new QuizzDTO();
        assertThat(quizzDTO1).isNotEqualTo(quizzDTO2);
        quizzDTO2.setId(quizzDTO1.getId());
        assertThat(quizzDTO1).isEqualTo(quizzDTO2);
        quizzDTO2.setId(UUID.randomUUID());
        assertThat(quizzDTO1).isNotEqualTo(quizzDTO2);
        quizzDTO1.setId(null);
        assertThat(quizzDTO1).isNotEqualTo(quizzDTO2);
    }
}
