package com.github.desprez.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.desprez.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AttemptDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttemptDTO.class);
        AttemptDTO attemptDTO1 = new AttemptDTO();
        attemptDTO1.setId(UUID.randomUUID());
        AttemptDTO attemptDTO2 = new AttemptDTO();
        assertThat(attemptDTO1).isNotEqualTo(attemptDTO2);
        attemptDTO2.setId(attemptDTO1.getId());
        assertThat(attemptDTO1).isEqualTo(attemptDTO2);
        attemptDTO2.setId(UUID.randomUUID());
        assertThat(attemptDTO1).isNotEqualTo(attemptDTO2);
        attemptDTO1.setId(null);
        assertThat(attemptDTO1).isNotEqualTo(attemptDTO2);
    }
}
