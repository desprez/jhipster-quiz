package com.github.desprez.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.desprez.IntegrationTest;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import com.github.desprez.domain.enumeration.DisplayOrder;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.service.QuizzService;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.mapper.QuizzMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link QuizzResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuizzResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Difficulty DEFAULT_DIFFICULTY = Difficulty.EASY;
    private static final Difficulty UPDATED_DIFFICULTY = Difficulty.MEDIUM;

    private static final Category DEFAULT_CATEGORY = Category.GENERAL_KNOWLEDGE;
    private static final Category UPDATED_CATEGORY = Category.BOOKS;

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final DisplayOrder DEFAULT_QUESTION_ORDER = DisplayOrder.RANDOM;
    private static final DisplayOrder UPDATED_QUESTION_ORDER = DisplayOrder.FIXED;

    private static final Integer DEFAULT_MAX_ANSWER_TIME = 1;
    private static final Integer UPDATED_MAX_ANSWER_TIME = 2;

    private static final Boolean DEFAULT_ROLLBACK_ALLOWED = false;
    private static final Boolean UPDATED_ROLLBACK_ALLOWED = true;

    private static final String ENTITY_API_URL = "/api/quizzes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private QuizzRepository quizzRepository;

    @Mock
    private QuizzRepository quizzRepositoryMock;

    @Autowired
    private QuizzMapper quizzMapper;

    @Mock
    private QuizzService quizzServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizzMockMvc;

    private Quizz quizz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quizz createEntity(EntityManager em) {
        Quizz quizz = new Quizz()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .difficulty(DEFAULT_DIFFICULTY)
            .category(DEFAULT_CATEGORY)
            .published(DEFAULT_PUBLISHED)
            .questionOrder(DEFAULT_QUESTION_ORDER)
            .maxAnswerTime(DEFAULT_MAX_ANSWER_TIME)
            .rollbackAllowed(DEFAULT_ROLLBACK_ALLOWED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        quizz.setUser(user);
        return quizz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quizz createUpdatedEntity(EntityManager em) {
        Quizz quizz = new Quizz()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .difficulty(UPDATED_DIFFICULTY)
            .category(UPDATED_CATEGORY)
            .published(UPDATED_PUBLISHED)
            .questionOrder(UPDATED_QUESTION_ORDER)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME)
            .rollbackAllowed(UPDATED_ROLLBACK_ALLOWED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        quizz.setUser(user);
        return quizz;
    }

    @BeforeEach
    public void initTest() {
        quizz = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizz() throws Exception {
        int databaseSizeBeforeCreate = quizzRepository.findAll().size();
        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);
        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isCreated());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeCreate + 1);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuizz.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQuizz.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
        assertThat(testQuizz.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testQuizz.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(DEFAULT_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(DEFAULT_MAX_ANSWER_TIME);
        assertThat(testQuizz.getRollbackAllowed()).isEqualTo(DEFAULT_ROLLBACK_ALLOWED);
    }

    @Test
    @Transactional
    void createQuizzWithExistingId() throws Exception {
        // Create the Quizz with an existing ID
        quizzRepository.saveAndFlush(quizz);
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        int databaseSizeBeforeCreate = quizzRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setTitle(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDifficultyIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setDifficulty(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setCategory(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPublishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setPublished(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuestionOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setQuestionOrder(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxAnswerTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setMaxAnswerTime(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRollbackAllowedIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setRollbackAllowed(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        restQuizzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isBadRequest());

        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizzes() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizz.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].questionOrder").value(hasItem(DEFAULT_QUESTION_ORDER.toString())))
            .andExpect(jsonPath("$.[*].maxAnswerTime").value(hasItem(DEFAULT_MAX_ANSWER_TIME)))
            .andExpect(jsonPath("$.[*].rollbackAllowed").value(hasItem(DEFAULT_ROLLBACK_ALLOWED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuizzesWithEagerRelationshipsIsEnabled() throws Exception {
        when(quizzServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuizzMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(quizzServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuizzesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(quizzServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuizzMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(quizzRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getQuizz() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get the quizz
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL_ID, quizz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizz.getId().toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.questionOrder").value(DEFAULT_QUESTION_ORDER.toString()))
            .andExpect(jsonPath("$.maxAnswerTime").value(DEFAULT_MAX_ANSWER_TIME))
            .andExpect(jsonPath("$.rollbackAllowed").value(DEFAULT_ROLLBACK_ALLOWED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingQuizz() throws Exception {
        // Get the quizz
        restQuizzMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizz() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();

        // Update the quizz
        Quizz updatedQuizz = quizzRepository.findById(quizz.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuizz are not directly saved in db
        em.detach(updatedQuizz);
        updatedQuizz
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .difficulty(UPDATED_DIFFICULTY)
            .category(UPDATED_CATEGORY)
            .published(UPDATED_PUBLISHED)
            .questionOrder(UPDATED_QUESTION_ORDER)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME)
            .rollbackAllowed(UPDATED_ROLLBACK_ALLOWED);
        QuizzDTO quizzDTO = quizzMapper.toDto(updatedQuizz);

        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizz.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuizz.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testQuizz.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testQuizz.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(UPDATED_MAX_ANSWER_TIME);
        assertThat(testQuizz.getRollbackAllowed()).isEqualTo(UPDATED_ROLLBACK_ALLOWED);
    }

    @Test
    @Transactional
    void putNonExistingQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizz.setId(UUID.randomUUID());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizz.setId(UUID.randomUUID());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizz.setId(UUID.randomUUID());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizzWithPatch() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();

        // Update the quizz using partial update
        Quizz partialUpdatedQuizz = new Quizz();
        partialUpdatedQuizz.setId(quizz.getId());

        partialUpdatedQuizz
            .description(UPDATED_DESCRIPTION)
            .difficulty(UPDATED_DIFFICULTY)
            .published(UPDATED_PUBLISHED)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME);

        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizz))
            )
            .andExpect(status().isOk());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuizz.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuizz.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testQuizz.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testQuizz.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(DEFAULT_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(UPDATED_MAX_ANSWER_TIME);
        assertThat(testQuizz.getRollbackAllowed()).isEqualTo(DEFAULT_ROLLBACK_ALLOWED);
    }

    @Test
    @Transactional
    void fullUpdateQuizzWithPatch() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();

        // Update the quizz using partial update
        Quizz partialUpdatedQuizz = new Quizz();
        partialUpdatedQuizz.setId(quizz.getId());

        partialUpdatedQuizz
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .difficulty(UPDATED_DIFFICULTY)
            .category(UPDATED_CATEGORY)
            .published(UPDATED_PUBLISHED)
            .questionOrder(UPDATED_QUESTION_ORDER)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME)
            .rollbackAllowed(UPDATED_ROLLBACK_ALLOWED);

        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizz))
            )
            .andExpect(status().isOk());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        Quizz testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizz.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQuizz.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
        assertThat(testQuizz.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testQuizz.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(UPDATED_MAX_ANSWER_TIME);
        assertThat(testQuizz.getRollbackAllowed()).isEqualTo(UPDATED_ROLLBACK_ALLOWED);
    }

    @Test
    @Transactional
    void patchNonExistingQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizz.setId(UUID.randomUUID());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizzDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizz.setId(UUID.randomUUID());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizz.setId(UUID.randomUUID());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(quizzDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quizz in the database
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizz() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        int databaseSizeBeforeDelete = quizzRepository.findAll().size();

        // Delete the quizz
        restQuizzMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizz.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Quizz> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
