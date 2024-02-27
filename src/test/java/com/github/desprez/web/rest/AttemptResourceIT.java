package com.github.desprez.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.desprez.IntegrationTest;
import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.service.AttemptService;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.mapper.AttemptMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
 * Integration tests for the {@link AttemptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AttemptResourceIT {

    private static final Integer DEFAULT_CORRECT_ANSWER_COUNT = 0;
    private static final Integer UPDATED_CORRECT_ANSWER_COUNT = 1;

    private static final Integer DEFAULT_WRONG_ANSWER_COUNT = 0;
    private static final Integer UPDATED_WRONG_ANSWER_COUNT = 1;

    private static final Integer DEFAULT_UNANSWERED_COUNT = 0;
    private static final Integer UPDATED_UNANSWERED_COUNT = 1;

    private static final Instant DEFAULT_STARTED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/attempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttemptRepository attemptRepository;

    @Mock
    private AttemptRepository attemptRepositoryMock;

    @Autowired
    private AttemptMapper attemptMapper;

    @Mock
    private AttemptService attemptServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAttemptMockMvc;

    private Attempt attempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attempt createEntity(EntityManager em) {
        Attempt attempt = new Attempt()
            .correctAnswerCount(DEFAULT_CORRECT_ANSWER_COUNT)
            .wrongAnswerCount(DEFAULT_WRONG_ANSWER_COUNT)
            .unansweredCount(DEFAULT_UNANSWERED_COUNT)
            .started(DEFAULT_STARTED)
            .ended(DEFAULT_ENDED);
        // Add required entity
        Quizz quizz;
        if (TestUtil.findAll(em, Quizz.class).isEmpty()) {
            quizz = QuizzResourceIT.createEntity(em);
            em.persist(quizz);
            em.flush();
        } else {
            quizz = TestUtil.findAll(em, Quizz.class).get(0);
        }
        attempt.setQuizz(quizz);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        attempt.setUser(user);
        return attempt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attempt createUpdatedEntity(EntityManager em) {
        Attempt attempt = new Attempt()
            .correctAnswerCount(UPDATED_CORRECT_ANSWER_COUNT)
            .wrongAnswerCount(UPDATED_WRONG_ANSWER_COUNT)
            .unansweredCount(UPDATED_UNANSWERED_COUNT)
            .started(UPDATED_STARTED)
            .ended(UPDATED_ENDED);
        // Add required entity
        Quizz quizz;
        if (TestUtil.findAll(em, Quizz.class).isEmpty()) {
            quizz = QuizzResourceIT.createUpdatedEntity(em);
            em.persist(quizz);
            em.flush();
        } else {
            quizz = TestUtil.findAll(em, Quizz.class).get(0);
        }
        attempt.setQuizz(quizz);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        attempt.setUser(user);
        return attempt;
    }

    @BeforeEach
    public void initTest() {
        attempt = createEntity(em);
    }

    @Test
    @Transactional
    void createAttempt() throws Exception {
        int databaseSizeBeforeCreate = attemptRepository.findAll().size();
        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);
        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isCreated());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeCreate + 1);
        Attempt testAttempt = attemptList.get(attemptList.size() - 1);
        assertThat(testAttempt.getCorrectAnswerCount()).isEqualTo(DEFAULT_CORRECT_ANSWER_COUNT);
        assertThat(testAttempt.getWrongAnswerCount()).isEqualTo(DEFAULT_WRONG_ANSWER_COUNT);
        assertThat(testAttempt.getUnansweredCount()).isEqualTo(DEFAULT_UNANSWERED_COUNT);
        assertThat(testAttempt.getStarted()).isEqualTo(DEFAULT_STARTED);
        assertThat(testAttempt.getEnded()).isEqualTo(DEFAULT_ENDED);
    }

    @Test
    @Transactional
    void createAttemptWithExistingId() throws Exception {
        // Create the Attempt with an existing ID
        attemptRepository.saveAndFlush(attempt);
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        int databaseSizeBeforeCreate = attemptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCorrectAnswercountIsRequired() throws Exception {
        int databaseSizeBeforeTest = attemptRepository.findAll().size();
        // set the field null
        attempt.setCorrectAnswerCount(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWrongAnswerCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = attemptRepository.findAll().size();
        // set the field null
        attempt.setWrongAnswerCount(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnansweredCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = attemptRepository.findAll().size();
        // set the field null
        attempt.setUnansweredCount(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartedIsRequired() throws Exception {
        int databaseSizeBeforeTest = attemptRepository.findAll().size();
        // set the field null
        attempt.setStarted(null);

        // Create the Attempt, which fails.
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        restAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isBadRequest());

        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAttempts() throws Exception {
        // Initialize the database
        attemptRepository.saveAndFlush(attempt);

        // Get all the attemptList
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attempt.getId().toString())))
            .andExpect(jsonPath("$.[*].correctAnswerCount").value(hasItem(DEFAULT_CORRECT_ANSWER_COUNT)))
            .andExpect(jsonPath("$.[*].wrongAnswerCount").value(hasItem(DEFAULT_WRONG_ANSWER_COUNT)))
            .andExpect(jsonPath("$.[*].unansweredCount").value(hasItem(DEFAULT_UNANSWERED_COUNT)))
            .andExpect(jsonPath("$.[*].started").value(hasItem(DEFAULT_STARTED.toString())))
            .andExpect(jsonPath("$.[*].ended").value(hasItem(DEFAULT_ENDED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttemptsWithEagerRelationshipsIsEnabled() throws Exception {
        when(attemptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttemptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(attemptServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttemptsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(attemptServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAttemptMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(attemptRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAttempt() throws Exception {
        // Initialize the database
        attemptRepository.saveAndFlush(attempt);

        // Get the attempt
        restAttemptMockMvc
            .perform(get(ENTITY_API_URL_ID, attempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attempt.getId().toString()))
            .andExpect(jsonPath("$.correctAnswerCount").value(DEFAULT_CORRECT_ANSWER_COUNT))
            .andExpect(jsonPath("$.wrongAnswerCount").value(DEFAULT_WRONG_ANSWER_COUNT))
            .andExpect(jsonPath("$.unansweredCount").value(DEFAULT_UNANSWERED_COUNT))
            .andExpect(jsonPath("$.started").value(DEFAULT_STARTED.toString()))
            .andExpect(jsonPath("$.ended").value(DEFAULT_ENDED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAttempt() throws Exception {
        // Get the attempt
        restAttemptMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAttempt() throws Exception {
        // Initialize the database
        attemptRepository.saveAndFlush(attempt);

        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();

        // Update the attempt
        Attempt updatedAttempt = attemptRepository.findById(attempt.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAttempt are not directly saved in db
        em.detach(updatedAttempt);
        updatedAttempt
            .correctAnswerCount(UPDATED_CORRECT_ANSWER_COUNT)
            .wrongAnswerCount(UPDATED_WRONG_ANSWER_COUNT)
            .unansweredCount(UPDATED_UNANSWERED_COUNT)
            .started(UPDATED_STARTED)
            .ended(UPDATED_ENDED);
        AttemptDTO attemptDTO = attemptMapper.toDto(updatedAttempt);

        restAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attemptDTO))
            )
            .andExpect(status().isOk());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
        Attempt testAttempt = attemptList.get(attemptList.size() - 1);
        assertThat(testAttempt.getCorrectAnswerCount()).isEqualTo(UPDATED_CORRECT_ANSWER_COUNT);
        assertThat(testAttempt.getWrongAnswerCount()).isEqualTo(UPDATED_WRONG_ANSWER_COUNT);
        assertThat(testAttempt.getUnansweredCount()).isEqualTo(UPDATED_UNANSWERED_COUNT);
        assertThat(testAttempt.getStarted()).isEqualTo(UPDATED_STARTED);
        assertThat(testAttempt.getEnded()).isEqualTo(UPDATED_ENDED);
    }

    @Test
    @Transactional
    void putNonExistingAttempt() throws Exception {
        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();
        attempt.setId(UUID.randomUUID());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attemptDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAttempt() throws Exception {
        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();
        attempt.setId(UUID.randomUUID());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAttempt() throws Exception {
        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();
        attempt.setId(UUID.randomUUID());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(attemptDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAttemptWithPatch() throws Exception {
        // Initialize the database
        attemptRepository.saveAndFlush(attempt);

        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();

        // Update the attempt using partial update
        Attempt partialUpdatedAttempt = new Attempt();
        partialUpdatedAttempt.setId(attempt.getId());

        partialUpdatedAttempt
            .correctAnswerCount(UPDATED_CORRECT_ANSWER_COUNT)
            .wrongAnswerCount(UPDATED_WRONG_ANSWER_COUNT)
            .ended(UPDATED_ENDED);

        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
        Attempt testAttempt = attemptList.get(attemptList.size() - 1);
        assertThat(testAttempt.getCorrectAnswerCount()).isEqualTo(UPDATED_CORRECT_ANSWER_COUNT);
        assertThat(testAttempt.getWrongAnswerCount()).isEqualTo(UPDATED_WRONG_ANSWER_COUNT);
        assertThat(testAttempt.getUnansweredCount()).isEqualTo(DEFAULT_UNANSWERED_COUNT);
        assertThat(testAttempt.getStarted()).isEqualTo(DEFAULT_STARTED);
        assertThat(testAttempt.getEnded()).isEqualTo(UPDATED_ENDED);
    }

    @Test
    @Transactional
    void fullUpdateAttemptWithPatch() throws Exception {
        // Initialize the database
        attemptRepository.saveAndFlush(attempt);

        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();

        // Update the attempt using partial update
        Attempt partialUpdatedAttempt = new Attempt();
        partialUpdatedAttempt.setId(attempt.getId());

        partialUpdatedAttempt
            .correctAnswerCount(UPDATED_CORRECT_ANSWER_COUNT)
            .wrongAnswerCount(UPDATED_WRONG_ANSWER_COUNT)
            .unansweredCount(UPDATED_UNANSWERED_COUNT)
            .started(UPDATED_STARTED)
            .ended(UPDATED_ENDED);

        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttempt))
            )
            .andExpect(status().isOk());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
        Attempt testAttempt = attemptList.get(attemptList.size() - 1);
        assertThat(testAttempt.getCorrectAnswerCount()).isEqualTo(UPDATED_CORRECT_ANSWER_COUNT);
        assertThat(testAttempt.getWrongAnswerCount()).isEqualTo(UPDATED_WRONG_ANSWER_COUNT);
        assertThat(testAttempt.getUnansweredCount()).isEqualTo(UPDATED_UNANSWERED_COUNT);
        assertThat(testAttempt.getStarted()).isEqualTo(UPDATED_STARTED);
        assertThat(testAttempt.getEnded()).isEqualTo(UPDATED_ENDED);
    }

    @Test
    @Transactional
    void patchNonExistingAttempt() throws Exception {
        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();
        attempt.setId(UUID.randomUUID());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attemptDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAttempt() throws Exception {
        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();
        attempt.setId(UUID.randomUUID());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attemptDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAttempt() throws Exception {
        int databaseSizeBeforeUpdate = attemptRepository.findAll().size();
        attempt.setId(UUID.randomUUID());

        // Create the Attempt
        AttemptDTO attemptDTO = attemptMapper.toDto(attempt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(attemptDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Attempt in the database
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAttempt() throws Exception {
        // Initialize the database
        attemptRepository.saveAndFlush(attempt);

        int databaseSizeBeforeDelete = attemptRepository.findAll().size();

        // Delete the attempt
        restAttemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, attempt.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attempt> attemptList = attemptRepository.findAll();
        assertThat(attemptList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
