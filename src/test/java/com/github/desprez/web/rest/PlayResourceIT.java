package com.github.desprez.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.desprez.IntegrationTest;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.service.PlayService;
import com.github.desprez.service.dto.AttemptDTO;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Integration tests for the {@link PlayResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlayResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PlayService playService;

    @Mock
    private AttemptRepository attemptRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void play_shouldReturnAttemptDTO() throws Exception {
        UUID quizId = UUID.randomUUID();
        AttemptDTO attemptDTO = new AttemptDTO();
        attemptDTO.setId(UUID.randomUUID());

        when(playService.start(quizId)).thenReturn(attemptDTO);

        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/play/{id}", quizId).contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(attemptDTO.getId().toString()));
    }

    @Test
    void evaluate_shouldReturnUpdatedAttemptDTO() throws Exception {
        UUID attemptId = UUID.randomUUID();
        AttemptDTO attemptDTO = new AttemptDTO();
        attemptDTO.setId(attemptId);

        when(attemptRepository.existsById(attemptId)).thenReturn(true);
        when(playService.evaluate(any(AttemptDTO.class))).thenReturn(attemptDTO);

        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put("/api/play/evaluate/{id}", attemptId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"id\":\"" + attemptId + "\",\"user\":{\"login\":\"testuser\"}}")
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(attemptDTO.getId().toString()));
    }
}
