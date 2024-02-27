package com.github.desprez.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.desprez.IntegrationTest;
import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import com.github.desprez.domain.enumeration.DisplayOrder;
import com.github.desprez.domain.enumeration.Period;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.service.QuizzService;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.mapper.QuizzMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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

    private static final DisplayOrder DEFAULT_QUESTION_ORDER = DisplayOrder.RANDOM;
    private static final DisplayOrder UPDATED_QUESTION_ORDER = DisplayOrder.FIXED;

    private static final Integer DEFAULT_MAX_ANSWER_TIME = 1;
    private static final Integer UPDATED_MAX_ANSWER_TIME = 2;
    private static final Integer SMALLER_MAX_ANSWER_TIME = 1 - 1;

    private static final Boolean DEFAULT_ALLOW_BACK = false;
    private static final Boolean UPDATED_ALLOW_BACK = true;

    private static final Boolean DEFAULT_ALLOW_REVIEW = false;
    private static final Boolean UPDATED_ALLOW_REVIEW = true;

    private static final Boolean DEFAULT_KEEP_ANSWERS_SECRET = false;
    private static final Boolean UPDATED_KEEP_ANSWERS_SECRET = true;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_PUBLISHED = false;
    private static final Boolean UPDATED_PUBLISHED = true;

    private static final Instant DEFAULT_PUBLISH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLISH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ATTEMPS_LIMIT = 1;
    private static final Integer UPDATED_ATTEMPS_LIMIT = 2;
    private static final Integer SMALLER_ATTEMPS_LIMIT = 1 - 1;

    private static final Period DEFAULT_ATTEMPS_LIMIT_PERIOD = Period.HOUR;
    private static final Period UPDATED_ATTEMPS_LIMIT_PERIOD = Period.DAY;

    private static final Integer DEFAULT_QUESTION_COUNT = 1;
    private static final Integer UPDATED_QUESTION_COUNT = 2;
    private static final Integer SMALLER_QUESTION_COUNT = 1 - 1;

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
            .questionOrder(DEFAULT_QUESTION_ORDER)
            .maxAnswerTime(DEFAULT_MAX_ANSWER_TIME)
            .allowBack(DEFAULT_ALLOW_BACK)
            .allowReview(DEFAULT_ALLOW_REVIEW)
            .keepAnswersSecret(DEFAULT_KEEP_ANSWERS_SECRET)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .published(DEFAULT_PUBLISHED)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .attempsLimit(DEFAULT_ATTEMPS_LIMIT)
            .attempsLimitPeriod(DEFAULT_ATTEMPS_LIMIT_PERIOD)
            .questionCount(DEFAULT_QUESTION_COUNT)
            .addQuestions(
                new Question()
                    .statement("question1")
                    .correctOptionIndex(1)
                    .addOptions(new Option().statement("A"))
                    .addOptions(new Option().statement("B"))
            )
            .addQuestions(
                new Question()
                    .statement("question2")
                    .correctOptionIndex(2)
                    .addOptions(new Option().statement("C"))
                    .addOptions(new Option().statement("D"))
            );
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
            .questionOrder(UPDATED_QUESTION_ORDER)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME)
            .allowBack(UPDATED_ALLOW_BACK)
            .allowReview(UPDATED_ALLOW_REVIEW)
            .keepAnswersSecret(UPDATED_KEEP_ANSWERS_SECRET)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .published(UPDATED_PUBLISHED)
            .publishDate(UPDATED_PUBLISH_DATE)
            .attempsLimit(UPDATED_ATTEMPS_LIMIT)
            .attempsLimitPeriod(UPDATED_ATTEMPS_LIMIT_PERIOD)
            .questionCount(UPDATED_QUESTION_COUNT);
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
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(DEFAULT_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(DEFAULT_MAX_ANSWER_TIME);
        assertThat(testQuizz.getAllowBack()).isEqualTo(DEFAULT_ALLOW_BACK);
        assertThat(testQuizz.getAllowReview()).isEqualTo(DEFAULT_ALLOW_REVIEW);
        assertThat(testQuizz.getKeepAnswersSecret()).isEqualTo(DEFAULT_KEEP_ANSWERS_SECRET);
        assertThat(testQuizz.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testQuizz.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testQuizz.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testQuizz.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testQuizz.getAttempsLimit()).isEqualTo(DEFAULT_ATTEMPS_LIMIT);
        assertThat(testQuizz.getAttempsLimitPeriod()).isEqualTo(DEFAULT_ATTEMPS_LIMIT_PERIOD);
        assertThat(testQuizz.getQuestionCount()).isEqualTo(DEFAULT_QUESTION_COUNT);
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
    void checkAllowBackIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setAllowBack(null);

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
    void checkAllowReviewIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setAllowReview(null);

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
    void checkKeepAnswersSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizz.setKeepAnswersSecret(null);

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
            .andExpect(jsonPath("$.[*].questionOrder").value(hasItem(DEFAULT_QUESTION_ORDER.toString())))
            .andExpect(jsonPath("$.[*].maxAnswerTime").value(hasItem(DEFAULT_MAX_ANSWER_TIME)))
            .andExpect(jsonPath("$.[*].allowBack").value(hasItem(DEFAULT_ALLOW_BACK.booleanValue())))
            .andExpect(jsonPath("$.[*].allowReview").value(hasItem(DEFAULT_ALLOW_REVIEW.booleanValue())))
            .andExpect(jsonPath("$.[*].keepAnswersSecret").value(hasItem(DEFAULT_KEEP_ANSWERS_SECRET.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].attempsLimit").value(hasItem(DEFAULT_ATTEMPS_LIMIT)))
            .andExpect(jsonPath("$.[*].attempsLimitPeriod").value(hasItem(DEFAULT_ATTEMPS_LIMIT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].questionCount").value(hasItem(DEFAULT_QUESTION_COUNT)));
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
            .andExpect(jsonPath("$.questionOrder").value(DEFAULT_QUESTION_ORDER.toString()))
            .andExpect(jsonPath("$.maxAnswerTime").value(DEFAULT_MAX_ANSWER_TIME))
            .andExpect(jsonPath("$.allowBack").value(DEFAULT_ALLOW_BACK.booleanValue()))
            .andExpect(jsonPath("$.allowReview").value(DEFAULT_ALLOW_REVIEW.booleanValue()))
            .andExpect(jsonPath("$.keepAnswersSecret").value(DEFAULT_KEEP_ANSWERS_SECRET.booleanValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.published").value(DEFAULT_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
            .andExpect(jsonPath("$.attempsLimit").value(DEFAULT_ATTEMPS_LIMIT))
            .andExpect(jsonPath("$.attempsLimitPeriod").value(DEFAULT_ATTEMPS_LIMIT_PERIOD.toString()))
            .andExpect(jsonPath("$.questionCount").value(DEFAULT_QUESTION_COUNT));
    }

    @Test
    @Transactional
    void getQuizzesByIdFiltering() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        UUID id = quizz.getId();

        defaultQuizzShouldBeFound("id.equals=" + id);
        defaultQuizzShouldNotBeFound("id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllQuizzesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where title equals to DEFAULT_TITLE
        defaultQuizzShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the quizzList where title equals to UPDATED_TITLE
        defaultQuizzShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuizzesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultQuizzShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the quizzList where title equals to UPDATED_TITLE
        defaultQuizzShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuizzesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where title is not null
        defaultQuizzShouldBeFound("title.specified=true");

        // Get all the quizzList where title is null
        defaultQuizzShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByTitleContainsSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where title contains DEFAULT_TITLE
        defaultQuizzShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the quizzList where title contains UPDATED_TITLE
        defaultQuizzShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuizzesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where title does not contain DEFAULT_TITLE
        defaultQuizzShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the quizzList where title does not contain UPDATED_TITLE
        defaultQuizzShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllQuizzesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where description equals to DEFAULT_DESCRIPTION
        defaultQuizzShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the quizzList where description equals to UPDATED_DESCRIPTION
        defaultQuizzShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuizzesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultQuizzShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the quizzList where description equals to UPDATED_DESCRIPTION
        defaultQuizzShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuizzesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where description is not null
        defaultQuizzShouldBeFound("description.specified=true");

        // Get all the quizzList where description is null
        defaultQuizzShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where description contains DEFAULT_DESCRIPTION
        defaultQuizzShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the quizzList where description contains UPDATED_DESCRIPTION
        defaultQuizzShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuizzesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where description does not contain DEFAULT_DESCRIPTION
        defaultQuizzShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the quizzList where description does not contain UPDATED_DESCRIPTION
        defaultQuizzShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllQuizzesByDifficultyIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where difficulty equals to DEFAULT_DIFFICULTY
        defaultQuizzShouldBeFound("difficulty.equals=" + DEFAULT_DIFFICULTY);

        // Get all the quizzList where difficulty equals to UPDATED_DIFFICULTY
        defaultQuizzShouldNotBeFound("difficulty.equals=" + UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void getAllQuizzesByDifficultyIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where difficulty in DEFAULT_DIFFICULTY or UPDATED_DIFFICULTY
        defaultQuizzShouldBeFound("difficulty.in=" + DEFAULT_DIFFICULTY + "," + UPDATED_DIFFICULTY);

        // Get all the quizzList where difficulty equals to UPDATED_DIFFICULTY
        defaultQuizzShouldNotBeFound("difficulty.in=" + UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void getAllQuizzesByDifficultyIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where difficulty is not null
        defaultQuizzShouldBeFound("difficulty.specified=true");

        // Get all the quizzList where difficulty is null
        defaultQuizzShouldNotBeFound("difficulty.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where category equals to DEFAULT_CATEGORY
        defaultQuizzShouldBeFound("category.equals=" + DEFAULT_CATEGORY);

        // Get all the quizzList where category equals to UPDATED_CATEGORY
        defaultQuizzShouldNotBeFound("category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllQuizzesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where category in DEFAULT_CATEGORY or UPDATED_CATEGORY
        defaultQuizzShouldBeFound("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY);

        // Get all the quizzList where category equals to UPDATED_CATEGORY
        defaultQuizzShouldNotBeFound("category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllQuizzesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where category is not null
        defaultQuizzShouldBeFound("category.specified=true");

        // Get all the quizzList where category is null
        defaultQuizzShouldNotBeFound("category.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionOrder equals to DEFAULT_QUESTION_ORDER
        defaultQuizzShouldBeFound("questionOrder.equals=" + DEFAULT_QUESTION_ORDER);

        // Get all the quizzList where questionOrder equals to UPDATED_QUESTION_ORDER
        defaultQuizzShouldNotBeFound("questionOrder.equals=" + UPDATED_QUESTION_ORDER);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionOrderIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionOrder in DEFAULT_QUESTION_ORDER or UPDATED_QUESTION_ORDER
        defaultQuizzShouldBeFound("questionOrder.in=" + DEFAULT_QUESTION_ORDER + "," + UPDATED_QUESTION_ORDER);

        // Get all the quizzList where questionOrder equals to UPDATED_QUESTION_ORDER
        defaultQuizzShouldNotBeFound("questionOrder.in=" + UPDATED_QUESTION_ORDER);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionOrder is not null
        defaultQuizzShouldBeFound("questionOrder.specified=true");

        // Get all the quizzList where questionOrder is null
        defaultQuizzShouldNotBeFound("questionOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime equals to DEFAULT_MAX_ANSWER_TIME
        defaultQuizzShouldBeFound("maxAnswerTime.equals=" + DEFAULT_MAX_ANSWER_TIME);

        // Get all the quizzList where maxAnswerTime equals to UPDATED_MAX_ANSWER_TIME
        defaultQuizzShouldNotBeFound("maxAnswerTime.equals=" + UPDATED_MAX_ANSWER_TIME);
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime in DEFAULT_MAX_ANSWER_TIME or UPDATED_MAX_ANSWER_TIME
        defaultQuizzShouldBeFound("maxAnswerTime.in=" + DEFAULT_MAX_ANSWER_TIME + "," + UPDATED_MAX_ANSWER_TIME);

        // Get all the quizzList where maxAnswerTime equals to UPDATED_MAX_ANSWER_TIME
        defaultQuizzShouldNotBeFound("maxAnswerTime.in=" + UPDATED_MAX_ANSWER_TIME);
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime is not null
        defaultQuizzShouldBeFound("maxAnswerTime.specified=true");

        // Get all the quizzList where maxAnswerTime is null
        defaultQuizzShouldNotBeFound("maxAnswerTime.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime is greater than or equal to DEFAULT_MAX_ANSWER_TIME
        defaultQuizzShouldBeFound("maxAnswerTime.greaterThanOrEqual=" + DEFAULT_MAX_ANSWER_TIME);

        // Get all the quizzList where maxAnswerTime is greater than or equal to UPDATED_MAX_ANSWER_TIME
        defaultQuizzShouldNotBeFound("maxAnswerTime.greaterThanOrEqual=" + UPDATED_MAX_ANSWER_TIME);
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime is less than or equal to DEFAULT_MAX_ANSWER_TIME
        defaultQuizzShouldBeFound("maxAnswerTime.lessThanOrEqual=" + DEFAULT_MAX_ANSWER_TIME);

        // Get all the quizzList where maxAnswerTime is less than or equal to SMALLER_MAX_ANSWER_TIME
        defaultQuizzShouldNotBeFound("maxAnswerTime.lessThanOrEqual=" + SMALLER_MAX_ANSWER_TIME);
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime is less than DEFAULT_MAX_ANSWER_TIME
        defaultQuizzShouldNotBeFound("maxAnswerTime.lessThan=" + DEFAULT_MAX_ANSWER_TIME);

        // Get all the quizzList where maxAnswerTime is less than UPDATED_MAX_ANSWER_TIME
        defaultQuizzShouldBeFound("maxAnswerTime.lessThan=" + UPDATED_MAX_ANSWER_TIME);
    }

    @Test
    @Transactional
    void getAllQuizzesByMaxAnswerTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where maxAnswerTime is greater than DEFAULT_MAX_ANSWER_TIME
        defaultQuizzShouldNotBeFound("maxAnswerTime.greaterThan=" + DEFAULT_MAX_ANSWER_TIME);

        // Get all the quizzList where maxAnswerTime is greater than SMALLER_MAX_ANSWER_TIME
        defaultQuizzShouldBeFound("maxAnswerTime.greaterThan=" + SMALLER_MAX_ANSWER_TIME);
    }

    @Test
    @Transactional
    void getAllQuizzesByAllowBackIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where allowBack equals to DEFAULT_ALLOW_BACK
        defaultQuizzShouldBeFound("allowBack.equals=" + DEFAULT_ALLOW_BACK);

        // Get all the quizzList where allowBack equals to UPDATED_ALLOW_BACK
        defaultQuizzShouldNotBeFound("allowBack.equals=" + UPDATED_ALLOW_BACK);
    }

    @Test
    @Transactional
    void getAllQuizzesByAllowBackIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where allowBack in DEFAULT_ALLOW_BACK or UPDATED_ALLOW_BACK
        defaultQuizzShouldBeFound("allowBack.in=" + DEFAULT_ALLOW_BACK + "," + UPDATED_ALLOW_BACK);

        // Get all the quizzList where allowBack equals to UPDATED_ALLOW_BACK
        defaultQuizzShouldNotBeFound("allowBack.in=" + UPDATED_ALLOW_BACK);
    }

    @Test
    @Transactional
    void getAllQuizzesByAllowBackIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where allowBack is not null
        defaultQuizzShouldBeFound("allowBack.specified=true");

        // Get all the quizzList where allowBack is null
        defaultQuizzShouldNotBeFound("allowBack.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByAllowReviewIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where allowReview equals to DEFAULT_ALLOW_REVIEW
        defaultQuizzShouldBeFound("allowReview.equals=" + DEFAULT_ALLOW_REVIEW);

        // Get all the quizzList where allowReview equals to UPDATED_ALLOW_REVIEW
        defaultQuizzShouldNotBeFound("allowReview.equals=" + UPDATED_ALLOW_REVIEW);
    }

    @Test
    @Transactional
    void getAllQuizzesByAllowReviewIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where allowReview in DEFAULT_ALLOW_REVIEW or UPDATED_ALLOW_REVIEW
        defaultQuizzShouldBeFound("allowReview.in=" + DEFAULT_ALLOW_REVIEW + "," + UPDATED_ALLOW_REVIEW);

        // Get all the quizzList where allowReview equals to UPDATED_ALLOW_REVIEW
        defaultQuizzShouldNotBeFound("allowReview.in=" + UPDATED_ALLOW_REVIEW);
    }

    @Test
    @Transactional
    void getAllQuizzesByAllowReviewIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where allowReview is not null
        defaultQuizzShouldBeFound("allowReview.specified=true");

        // Get all the quizzList where allowReview is null
        defaultQuizzShouldNotBeFound("allowReview.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByKeepAnswersSecretIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where keepAnswersSecret equals to DEFAULT_KEEP_ANSWERS_SECRET
        defaultQuizzShouldBeFound("keepAnswersSecret.equals=" + DEFAULT_KEEP_ANSWERS_SECRET);

        // Get all the quizzList where keepAnswersSecret equals to UPDATED_KEEP_ANSWERS_SECRET
        defaultQuizzShouldNotBeFound("keepAnswersSecret.equals=" + UPDATED_KEEP_ANSWERS_SECRET);
    }

    @Test
    @Transactional
    void getAllQuizzesByKeepAnswersSecretIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where keepAnswersSecret in DEFAULT_KEEP_ANSWERS_SECRET or UPDATED_KEEP_ANSWERS_SECRET
        defaultQuizzShouldBeFound("keepAnswersSecret.in=" + DEFAULT_KEEP_ANSWERS_SECRET + "," + UPDATED_KEEP_ANSWERS_SECRET);

        // Get all the quizzList where keepAnswersSecret equals to UPDATED_KEEP_ANSWERS_SECRET
        defaultQuizzShouldNotBeFound("keepAnswersSecret.in=" + UPDATED_KEEP_ANSWERS_SECRET);
    }

    @Test
    @Transactional
    void getAllQuizzesByKeepAnswersSecretIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where keepAnswersSecret is not null
        defaultQuizzShouldBeFound("keepAnswersSecret.specified=true");

        // Get all the quizzList where keepAnswersSecret is null
        defaultQuizzShouldNotBeFound("keepAnswersSecret.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByPublishedIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where published equals to DEFAULT_PUBLISHED
        defaultQuizzShouldBeFound("published.equals=" + DEFAULT_PUBLISHED);

        // Get all the quizzList where published equals to UPDATED_PUBLISHED
        defaultQuizzShouldNotBeFound("published.equals=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllQuizzesByPublishedIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where published in DEFAULT_PUBLISHED or UPDATED_PUBLISHED
        defaultQuizzShouldBeFound("published.in=" + DEFAULT_PUBLISHED + "," + UPDATED_PUBLISHED);

        // Get all the quizzList where published equals to UPDATED_PUBLISHED
        defaultQuizzShouldNotBeFound("published.in=" + UPDATED_PUBLISHED);
    }

    @Test
    @Transactional
    void getAllQuizzesByPublishedIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where published is not null
        defaultQuizzShouldBeFound("published.specified=true");

        // Get all the quizzList where published is null
        defaultQuizzShouldNotBeFound("published.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByPublishDateIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where publishDate equals to DEFAULT_PUBLISH_DATE
        defaultQuizzShouldBeFound("publishDate.equals=" + DEFAULT_PUBLISH_DATE);

        // Get all the quizzList where publishDate equals to UPDATED_PUBLISH_DATE
        defaultQuizzShouldNotBeFound("publishDate.equals=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    @Transactional
    void getAllQuizzesByPublishDateIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where publishDate in DEFAULT_PUBLISH_DATE or UPDATED_PUBLISH_DATE
        defaultQuizzShouldBeFound("publishDate.in=" + DEFAULT_PUBLISH_DATE + "," + UPDATED_PUBLISH_DATE);

        // Get all the quizzList where publishDate equals to UPDATED_PUBLISH_DATE
        defaultQuizzShouldNotBeFound("publishDate.in=" + UPDATED_PUBLISH_DATE);
    }

    @Test
    @Transactional
    void getAllQuizzesByPublishDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where publishDate is not null
        defaultQuizzShouldBeFound("publishDate.specified=true");

        // Get all the quizzList where publishDate is null
        defaultQuizzShouldNotBeFound("publishDate.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit equals to DEFAULT_ATTEMPS_LIMIT
        defaultQuizzShouldBeFound("attempsLimit.equals=" + DEFAULT_ATTEMPS_LIMIT);

        // Get all the quizzList where attempsLimit equals to UPDATED_ATTEMPS_LIMIT
        defaultQuizzShouldNotBeFound("attempsLimit.equals=" + UPDATED_ATTEMPS_LIMIT);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit in DEFAULT_ATTEMPS_LIMIT or UPDATED_ATTEMPS_LIMIT
        defaultQuizzShouldBeFound("attempsLimit.in=" + DEFAULT_ATTEMPS_LIMIT + "," + UPDATED_ATTEMPS_LIMIT);

        // Get all the quizzList where attempsLimit equals to UPDATED_ATTEMPS_LIMIT
        defaultQuizzShouldNotBeFound("attempsLimit.in=" + UPDATED_ATTEMPS_LIMIT);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit is not null
        defaultQuizzShouldBeFound("attempsLimit.specified=true");

        // Get all the quizzList where attempsLimit is null
        defaultQuizzShouldNotBeFound("attempsLimit.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit is greater than or equal to DEFAULT_ATTEMPS_LIMIT
        defaultQuizzShouldBeFound("attempsLimit.greaterThanOrEqual=" + DEFAULT_ATTEMPS_LIMIT);

        // Get all the quizzList where attempsLimit is greater than or equal to UPDATED_ATTEMPS_LIMIT
        defaultQuizzShouldNotBeFound("attempsLimit.greaterThanOrEqual=" + UPDATED_ATTEMPS_LIMIT);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit is less than or equal to DEFAULT_ATTEMPS_LIMIT
        defaultQuizzShouldBeFound("attempsLimit.lessThanOrEqual=" + DEFAULT_ATTEMPS_LIMIT);

        // Get all the quizzList where attempsLimit is less than or equal to SMALLER_ATTEMPS_LIMIT
        defaultQuizzShouldNotBeFound("attempsLimit.lessThanOrEqual=" + SMALLER_ATTEMPS_LIMIT);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit is less than DEFAULT_ATTEMPS_LIMIT
        defaultQuizzShouldNotBeFound("attempsLimit.lessThan=" + DEFAULT_ATTEMPS_LIMIT);

        // Get all the quizzList where attempsLimit is less than UPDATED_ATTEMPS_LIMIT
        defaultQuizzShouldBeFound("attempsLimit.lessThan=" + UPDATED_ATTEMPS_LIMIT);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimit is greater than DEFAULT_ATTEMPS_LIMIT
        defaultQuizzShouldNotBeFound("attempsLimit.greaterThan=" + DEFAULT_ATTEMPS_LIMIT);

        // Get all the quizzList where attempsLimit is greater than SMALLER_ATTEMPS_LIMIT
        defaultQuizzShouldBeFound("attempsLimit.greaterThan=" + SMALLER_ATTEMPS_LIMIT);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimitPeriod equals to DEFAULT_ATTEMPS_LIMIT_PERIOD
        defaultQuizzShouldBeFound("attempsLimitPeriod.equals=" + DEFAULT_ATTEMPS_LIMIT_PERIOD);

        // Get all the quizzList where attempsLimitPeriod equals to UPDATED_ATTEMPS_LIMIT_PERIOD
        defaultQuizzShouldNotBeFound("attempsLimitPeriod.equals=" + UPDATED_ATTEMPS_LIMIT_PERIOD);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitPeriodIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimitPeriod in DEFAULT_ATTEMPS_LIMIT_PERIOD or UPDATED_ATTEMPS_LIMIT_PERIOD
        defaultQuizzShouldBeFound("attempsLimitPeriod.in=" + DEFAULT_ATTEMPS_LIMIT_PERIOD + "," + UPDATED_ATTEMPS_LIMIT_PERIOD);

        // Get all the quizzList where attempsLimitPeriod equals to UPDATED_ATTEMPS_LIMIT_PERIOD
        defaultQuizzShouldNotBeFound("attempsLimitPeriod.in=" + UPDATED_ATTEMPS_LIMIT_PERIOD);
    }

    @Test
    @Transactional
    void getAllQuizzesByAttempsLimitPeriodIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where attempsLimitPeriod is not null
        defaultQuizzShouldBeFound("attempsLimitPeriod.specified=true");

        // Get all the quizzList where attempsLimitPeriod is null
        defaultQuizzShouldNotBeFound("attempsLimitPeriod.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount equals to DEFAULT_QUESTION_COUNT
        defaultQuizzShouldBeFound("questionCount.equals=" + DEFAULT_QUESTION_COUNT);

        // Get all the quizzList where questionCount equals to UPDATED_QUESTION_COUNT
        defaultQuizzShouldNotBeFound("questionCount.equals=" + UPDATED_QUESTION_COUNT);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsInShouldWork() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount in DEFAULT_QUESTION_COUNT or UPDATED_QUESTION_COUNT
        defaultQuizzShouldBeFound("questionCount.in=" + DEFAULT_QUESTION_COUNT + "," + UPDATED_QUESTION_COUNT);

        // Get all the quizzList where questionCount equals to UPDATED_QUESTION_COUNT
        defaultQuizzShouldNotBeFound("questionCount.in=" + UPDATED_QUESTION_COUNT);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount is not null
        defaultQuizzShouldBeFound("questionCount.specified=true");

        // Get all the quizzList where questionCount is null
        defaultQuizzShouldNotBeFound("questionCount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount is greater than or equal to DEFAULT_QUESTION_COUNT
        defaultQuizzShouldBeFound("questionCount.greaterThanOrEqual=" + DEFAULT_QUESTION_COUNT);

        // Get all the quizzList where questionCount is greater than or equal to UPDATED_QUESTION_COUNT
        defaultQuizzShouldNotBeFound("questionCount.greaterThanOrEqual=" + UPDATED_QUESTION_COUNT);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount is less than or equal to DEFAULT_QUESTION_COUNT
        defaultQuizzShouldBeFound("questionCount.lessThanOrEqual=" + DEFAULT_QUESTION_COUNT);

        // Get all the quizzList where questionCount is less than or equal to SMALLER_QUESTION_COUNT
        defaultQuizzShouldNotBeFound("questionCount.lessThanOrEqual=" + SMALLER_QUESTION_COUNT);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsLessThanSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount is less than DEFAULT_QUESTION_COUNT
        defaultQuizzShouldNotBeFound("questionCount.lessThan=" + DEFAULT_QUESTION_COUNT);

        // Get all the quizzList where questionCount is less than UPDATED_QUESTION_COUNT
        defaultQuizzShouldBeFound("questionCount.lessThan=" + UPDATED_QUESTION_COUNT);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizz);

        // Get all the quizzList where questionCount is greater than DEFAULT_QUESTION_COUNT
        defaultQuizzShouldNotBeFound("questionCount.greaterThan=" + DEFAULT_QUESTION_COUNT);

        // Get all the quizzList where questionCount is greater than SMALLER_QUESTION_COUNT
        defaultQuizzShouldBeFound("questionCount.greaterThan=" + SMALLER_QUESTION_COUNT);
    }

    @Test
    @Transactional
    void getAllQuizzesByQuestionsIsEqualToSomething() throws Exception {
        Question questions;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            quizzRepository.saveAndFlush(quizz);
            questions = QuestionResourceIT.createEntity(em);
        } else {
            questions = TestUtil.findAll(em, Question.class).get(0);
        }
        em.persist(questions);
        em.flush();
        quizz.addQuestions(questions);
        quizzRepository.saveAndFlush(quizz);
        UUID questionsId = questions.getId();
        // Get all the quizzList where questions equals to questionsId
        defaultQuizzShouldBeFound("questionsId.equals=" + questionsId);

        // Get all the quizzList where questions equals to UUID.randomUUID()
        defaultQuizzShouldNotBeFound("questionsId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllQuizzesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            quizzRepository.saveAndFlush(quizz);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        quizz.setUser(user);
        quizzRepository.saveAndFlush(quizz);
        Long userId = user.getId();
        // Get all the quizzList where user equals to userId
        defaultQuizzShouldBeFound("userId.equals=" + userId);

        // Get all the quizzList where user equals to (userId + 1)
        defaultQuizzShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuizzShouldBeFound(String filter) throws Exception {
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizz.getId().toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].questionOrder").value(hasItem(DEFAULT_QUESTION_ORDER.toString())))
            .andExpect(jsonPath("$.[*].maxAnswerTime").value(hasItem(DEFAULT_MAX_ANSWER_TIME)))
            .andExpect(jsonPath("$.[*].allowBack").value(hasItem(DEFAULT_ALLOW_BACK.booleanValue())))
            .andExpect(jsonPath("$.[*].allowReview").value(hasItem(DEFAULT_ALLOW_REVIEW.booleanValue())))
            .andExpect(jsonPath("$.[*].keepAnswersSecret").value(hasItem(DEFAULT_KEEP_ANSWERS_SECRET.booleanValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].published").value(hasItem(DEFAULT_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
            .andExpect(jsonPath("$.[*].attempsLimit").value(hasItem(DEFAULT_ATTEMPS_LIMIT)))
            .andExpect(jsonPath("$.[*].attempsLimitPeriod").value(hasItem(DEFAULT_ATTEMPS_LIMIT_PERIOD.toString())))
            .andExpect(jsonPath("$.[*].questionCount").value(hasItem(DEFAULT_QUESTION_COUNT)));

        // Check, that the count call also returns 1
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuizzShouldNotBeFound(String filter) throws Exception {
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .questionOrder(UPDATED_QUESTION_ORDER)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME)
            .allowBack(UPDATED_ALLOW_BACK)
            .allowReview(UPDATED_ALLOW_REVIEW)
            .keepAnswersSecret(UPDATED_KEEP_ANSWERS_SECRET)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .published(UPDATED_PUBLISHED)
            .publishDate(UPDATED_PUBLISH_DATE)
            .attempsLimit(UPDATED_ATTEMPS_LIMIT)
            .attempsLimitPeriod(UPDATED_ATTEMPS_LIMIT_PERIOD)
            .questionCount(UPDATED_QUESTION_COUNT);
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
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(UPDATED_MAX_ANSWER_TIME);
        assertThat(testQuizz.getAllowBack()).isEqualTo(UPDATED_ALLOW_BACK);
        assertThat(testQuizz.getAllowReview()).isEqualTo(UPDATED_ALLOW_REVIEW);
        assertThat(testQuizz.getKeepAnswersSecret()).isEqualTo(UPDATED_KEEP_ANSWERS_SECRET);
        assertThat(testQuizz.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuizz.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testQuizz.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testQuizz.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testQuizz.getAttempsLimit()).isEqualTo(UPDATED_ATTEMPS_LIMIT);
        assertThat(testQuizz.getAttempsLimitPeriod()).isEqualTo(UPDATED_ATTEMPS_LIMIT_PERIOD);
        assertThat(testQuizz.getQuestionCount()).isEqualTo(UPDATED_QUESTION_COUNT);
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
            .questionOrder(UPDATED_QUESTION_ORDER)
            .allowBack(UPDATED_ALLOW_BACK)
            .keepAnswersSecret(UPDATED_KEEP_ANSWERS_SECRET)
            .attempsLimit(UPDATED_ATTEMPS_LIMIT)
            .attempsLimitPeriod(UPDATED_ATTEMPS_LIMIT_PERIOD)
            .questionCount(UPDATED_QUESTION_COUNT);

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
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(DEFAULT_MAX_ANSWER_TIME);
        assertThat(testQuizz.getAllowBack()).isEqualTo(UPDATED_ALLOW_BACK);
        assertThat(testQuizz.getAllowReview()).isEqualTo(DEFAULT_ALLOW_REVIEW);
        assertThat(testQuizz.getKeepAnswersSecret()).isEqualTo(UPDATED_KEEP_ANSWERS_SECRET);
        assertThat(testQuizz.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testQuizz.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testQuizz.getPublished()).isEqualTo(DEFAULT_PUBLISHED);
        assertThat(testQuizz.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
        assertThat(testQuizz.getAttempsLimit()).isEqualTo(UPDATED_ATTEMPS_LIMIT);
        assertThat(testQuizz.getAttempsLimitPeriod()).isEqualTo(UPDATED_ATTEMPS_LIMIT_PERIOD);
        assertThat(testQuizz.getQuestionCount()).isEqualTo(UPDATED_QUESTION_COUNT);
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
            .questionOrder(UPDATED_QUESTION_ORDER)
            .maxAnswerTime(UPDATED_MAX_ANSWER_TIME)
            .allowBack(UPDATED_ALLOW_BACK)
            .allowReview(UPDATED_ALLOW_REVIEW)
            .keepAnswersSecret(UPDATED_KEEP_ANSWERS_SECRET)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .published(UPDATED_PUBLISHED)
            .publishDate(UPDATED_PUBLISH_DATE)
            .attempsLimit(UPDATED_ATTEMPS_LIMIT)
            .attempsLimitPeriod(UPDATED_ATTEMPS_LIMIT_PERIOD)
            .questionCount(UPDATED_QUESTION_COUNT);

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
        assertThat(testQuizz.getQuestionOrder()).isEqualTo(UPDATED_QUESTION_ORDER);
        assertThat(testQuizz.getMaxAnswerTime()).isEqualTo(UPDATED_MAX_ANSWER_TIME);
        assertThat(testQuizz.getAllowBack()).isEqualTo(UPDATED_ALLOW_BACK);
        assertThat(testQuizz.getAllowReview()).isEqualTo(UPDATED_ALLOW_REVIEW);
        assertThat(testQuizz.getKeepAnswersSecret()).isEqualTo(UPDATED_KEEP_ANSWERS_SECRET);
        assertThat(testQuizz.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testQuizz.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testQuizz.getPublished()).isEqualTo(UPDATED_PUBLISHED);
        assertThat(testQuizz.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
        assertThat(testQuizz.getAttempsLimit()).isEqualTo(UPDATED_ATTEMPS_LIMIT);
        assertThat(testQuizz.getAttempsLimitPeriod()).isEqualTo(UPDATED_ATTEMPS_LIMIT_PERIOD);
        assertThat(testQuizz.getQuestionCount()).isEqualTo(UPDATED_QUESTION_COUNT);
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
