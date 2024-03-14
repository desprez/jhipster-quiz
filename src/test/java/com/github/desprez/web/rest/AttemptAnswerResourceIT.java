package com.github.desprez.web.rest;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.desprez.IntegrationTest;
import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.repository.AttemptAnswerRepository;
import com.github.desprez.service.AttemptAnswerService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AttemptAnswerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AttemptAnswerResourceIT {

    private static final Instant DEFAULT_STARTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CORRECT = false;
    private static final Boolean UPDATED_CORRECT = true;

    private static final String ENTITY_API_URL = "/api/attempt-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttemptAnswerRepository attemptAnswerRepository;

    @Mock
    private AttemptAnswerRepository attemptAnswerRepositoryMock;

    @Mock
    private AttemptAnswerService attemptAnswerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttemptAnswerMockMvc;

    private AttemptAnswer attemptAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttemptAnswer createEntity(EntityManager em) {
        AttemptAnswer attemptAnswer = new AttemptAnswer().started(DEFAULT_STARTED).ended(DEFAULT_ENDED).correct(DEFAULT_CORRECT);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        attemptAnswer.setQuestion(question);
        // Add required entity
        Option option;
        if (TestUtil.findAll(em, Option.class).isEmpty()) {
            option = OptionResourceIT.createEntity(em);
            em.persist(option);
            em.flush();
        } else {
            option = TestUtil.findAll(em, Option.class).get(0);
        }
        attemptAnswer.setOption(option);
        return attemptAnswer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttemptAnswer createUpdatedEntity(EntityManager em) {
        AttemptAnswer attemptAnswer = new AttemptAnswer().started(UPDATED_STARTED).ended(UPDATED_ENDED);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createUpdatedEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        attemptAnswer.setQuestion(question);
        // Add required entity
        Option option;
        if (TestUtil.findAll(em, Option.class).isEmpty()) {
            option = OptionResourceIT.createUpdatedEntity(em);
            em.persist(option);
            em.flush();
        } else {
            option = TestUtil.findAll(em, Option.class).get(0);
        }
        attemptAnswer.setOption(option);
        return attemptAnswer;
    }

    @BeforeEach
    public void initTest() {
        attemptAnswer = createEntity(em);
    }

    @Test
    @Transactional
    void getAllAttemptAnswers() throws Exception {
        // Initialize the database
        attemptAnswerRepository.saveAndFlush(attemptAnswer);

        // Get all the attemptAnswerList
        restAttemptAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attemptAnswer.getId().toString())))
            .andExpect(jsonPath("$.[*].started").value(hasItem(DEFAULT_STARTED.toString())))
            .andExpect(jsonPath("$.[*].ended").value(hasItem(DEFAULT_ENDED.toString())))
            .andExpect(jsonPath("$.[*].correct").value(hasItem(DEFAULT_CORRECT.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttemptAnswersWithEagerRelationshipsIsEnabled() throws Exception {
        when(attemptAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttemptAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(attemptAnswerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttemptAnswersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(attemptAnswerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttemptAnswerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(attemptAnswerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAttemptAnswer() throws Exception {
        // Initialize the database
        attemptAnswerRepository.saveAndFlush(attemptAnswer);

        // Get the attemptAnswer
        restAttemptAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, attemptAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attemptAnswer.getId().toString()))
            .andExpect(jsonPath("$.started").value(DEFAULT_STARTED.toString()))
            .andExpect(jsonPath("$.ended").value(DEFAULT_ENDED.toString()))
            .andExpect(jsonPath("$.correct").value(DEFAULT_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAttemptAnswer() throws Exception {
        // Get the attemptAnswer
        restAttemptAnswerMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }
}
