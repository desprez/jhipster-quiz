package com.github.desprez.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.desprez.IntegrationTest;
import com.github.desprez.domain.Question;
import com.github.desprez.repository.QuestionRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    private static final String DEFAULT_STATEMENT = "AAAAAAAAAA";
    private static final String UPDATED_STATEMENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Integer DEFAULT_CORRECT_OPTION_INDEX = 1;
    private static final Integer UPDATED_CORRECT_OPTION_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .statement(DEFAULT_STATEMENT)
            .index(DEFAULT_INDEX)
            .correctOptionIndex(DEFAULT_CORRECT_OPTION_INDEX);
        return question;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .statement(UPDATED_STATEMENT)
            .index(UPDATED_INDEX)
            .correctOptionIndex(UPDATED_CORRECT_OPTION_INDEX);
        return question;
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();
        // Create the Question
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getStatement()).isEqualTo(DEFAULT_STATEMENT);
        assertThat(testQuestion.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testQuestion.getCorrectOptionIndex()).isEqualTo(DEFAULT_CORRECT_OPTION_INDEX);
    }

    @Test
    @Transactional
    void createQuestionWithExistingId() throws Exception {
        // Create the Question with an existing ID
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatementIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setStatement(null);

        // Create the Question, which fails.

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setIndex(null);

        // Create the Question, which fails.

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCorrectOptionIndexIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setCorrectOptionIndex(null);

        // Create the Question, which fails.

        restQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().toString())))
            .andExpect(jsonPath("$.[*].statement").value(hasItem(DEFAULT_STATEMENT)))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].correctOptionIndex").value(hasItem(DEFAULT_CORRECT_OPTION_INDEX)));
    }

    @Test
    @Transactional
    void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().toString()))
            .andExpect(jsonPath("$.statement").value(DEFAULT_STATEMENT))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.correctOptionIndex").value(DEFAULT_CORRECT_OPTION_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion.statement(UPDATED_STATEMENT).index(UPDATED_INDEX).correctOptionIndex(UPDATED_CORRECT_OPTION_INDEX);

        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getStatement()).isEqualTo(UPDATED_STATEMENT);
        assertThat(testQuestion.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testQuestion.getCorrectOptionIndex()).isEqualTo(UPDATED_CORRECT_OPTION_INDEX);
    }

    @Test
    @Transactional
    void putNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, question.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.statement(UPDATED_STATEMENT).correctOptionIndex(UPDATED_CORRECT_OPTION_INDEX);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getStatement()).isEqualTo(UPDATED_STATEMENT);
        assertThat(testQuestion.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testQuestion.getCorrectOptionIndex()).isEqualTo(UPDATED_CORRECT_OPTION_INDEX);
    }

    @Test
    @Transactional
    void fullUpdateQuestionWithPatch() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question using partial update
        Question partialUpdatedQuestion = new Question();
        partialUpdatedQuestion.setId(question.getId());

        partialUpdatedQuestion.statement(UPDATED_STATEMENT).index(UPDATED_INDEX).correctOptionIndex(UPDATED_CORRECT_OPTION_INDEX);

        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestion))
            )
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getStatement()).isEqualTo(UPDATED_STATEMENT);
        assertThat(testQuestion.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testQuestion.getCorrectOptionIndex()).isEqualTo(UPDATED_CORRECT_OPTION_INDEX);
    }

    @Test
    @Transactional
    void patchNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, question.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(question))
            )
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();
        question.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        restQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, question.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
