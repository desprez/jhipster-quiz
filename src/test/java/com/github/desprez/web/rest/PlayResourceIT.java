package com.github.desprez.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.github.desprez.IntegrationTest;
import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import com.github.desprez.domain.enumeration.DisplayOrder;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.service.dto.AttemptAnswerDTO;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.dto.OptionDTO;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private EntityManager em;

    public Quizz createQuizzEntity(EntityManager em) {
        Quizz quizz = new Quizz()
            .title("AAAAAAAAAA")
            .description("AAAAAAAAAA")
            .difficulty(Difficulty.EASY)
            .category(Category.GENERAL_KNOWLEDGE)
            .questionOrder(DisplayOrder.RANDOM)
            .maxAnswerTime(Duration.ofSeconds(300))
            .allowBack(false)
            .allowReview(false)
            .keepAnswersSecret(true)
            .published(true)
            .questionCount(3)
            .addQuestions(
                new Question()
                    .index(1)
                    .statement("question1")
                    .correctOptionIndex(1)
                    .addOptions(new Option().index(1).statement("A"))
                    .addOptions(new Option().index(2).statement("B"))
            )
            .addQuestions(
                new Question()
                    .index(2)
                    .statement("question2")
                    .correctOptionIndex(2)
                    .addOptions(new Option().index(1).statement("C"))
                    .addOptions(new Option().index(2).statement("D"))
            )
            .addQuestions(
                new Question()
                    .index(3)
                    .statement("question3")
                    .correctOptionIndex(1)
                    .addOptions(new Option().index(1).statement("E"))
                    .addOptions(new Option().index(2).statement("F"))
            );

        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        quizz.setUser(user);
        return quizz;
    }

    private OptionDTO findOptionByIndex(Set<Question> questions, int questionNumber, int choice) {
        Optional<Question> found = questions.stream().filter(q -> q.getIndex().equals(questionNumber)).findFirst();
        if (found.isPresent()) {
            Optional<Option> optionFound = findOptionByIndex(found.orElseThrow(), choice);
            OptionDTO optionDTO = new OptionDTO();
            optionDTO.setId(optionFound.orElse(null).getId());
            return optionDTO;
        }
        return null;
    }

    private Optional<Option> findOptionByIndex(Question question, int choice) {
        return question.getOptions().stream().filter(o -> o.getIndex().equals(choice)).findFirst();
    }

    @Transactional
    @Test
    void play_simulateAnswer_evaluate_should_return_scored_attempt() throws Exception {
        // Given
        var quizz = createQuizzEntity(em);
        quizzRepository.saveAndFlush(quizz);
        UUID quizId = quizz.getId();

        CustomResponseHandler<AttemptDTO> responseHandler = new CustomResponseHandler<>(AttemptDTO.class);

        // When
        mockMvc
            .perform(MockMvcRequestBuilders.get("/api/play/{id}", quizId).contentType(MediaType.APPLICATION_JSON))
            .andDo(responseHandler)
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn();

        // Then
        AttemptDTO attempt = responseHandler.getResponseObject();
        assertThat(attempt).isNotNull();
        assertThat(attempt.getQuizz().getId()).isEqualTo(quizId);
        assertThat(attempt.getAnswers())
            .hasSize(3)
            .extracting(answer -> answer.getQuestion().getStatement())
            .containsExactlyInAnyOrder("question1", "question2", "question3");

        // Given

        UUID attemptId = attempt.getId();

        simulateUserChoice(quizz.getQuestions(), attempt);

        // When
        mockMvc
            .perform(
                MockMvcRequestBuilders
                    .put("/api/play/evaluate/{id}", attemptId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attempt))
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.answers", hasSize(3)))
            .andExpect(jsonPath("$.correctAnswerCount", is(1)))
            .andExpect(jsonPath("$.wrongAnswerCount", is(1)))
            .andExpect(jsonPath("$.unansweredCount", is(1)))
            .andExpect(jsonPath("$.answers[*].correct", containsInAnyOrder(true, false)));
    }

    private void simulateUserChoice(Set<Question> questions, AttemptDTO attempt) {
        // Simulate correct answer
        Optional<AttemptAnswerDTO> answer1 = attempt
            .getAnswers()
            .stream()
            .filter(answer -> "question1".equals(answer.getQuestion().getStatement()))
            .findFirst();

        answer1.ifPresent(answer -> {
            answer.setOption(findOptionByIndex(questions, 1, 1));
        });

        // Simulate wrong answer
        Optional<AttemptAnswerDTO> answer2 = attempt
            .getAnswers()
            .stream()
            .filter(answer -> "question2".equals(answer.getQuestion().getStatement()))
            .findFirst();

        answer2.ifPresent(answer -> {
            answer.setOption(findOptionByIndex(questions, 2, 1));
        });
    }
}
