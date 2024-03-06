package com.github.desprez.web.client;

import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.web.rest.PlayResource;
import com.opentdb.api.client.dto.OpenDBResponse;
import com.opentdb.api.client.dto.Result;
import jakarta.validation.constraints.Max;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenDBRestClient {

    private final Logger log = LoggerFactory.getLogger(OpenDBRestClient.class);

    private RestTemplate restTemplate;

    public OpenDBRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // {"trivia_categories":[{"id":9,"name":"General Knowledge"},{"id":10,"name":"Entertainment:
    // Books"},{"id":11,"name":"Entertainment: Film"},{"id":12,"name":"Entertainment:
    // Music"},{"id":13,"name":"Entertainment: Musicals & Theatres"},{"id":14,"name":"Entertainment:
    // Television"},{"id":15,"name":"Entertainment: Video Games"},{"id":16,"name":"Entertainment:
    // Board Games"},{"id":17,"name":"Science & Nature"},{"id":18,"name":"Science:
    // Computers"},{"id":19,"name":"Science:
    // Mathematics"},{"id":20,"name":"Mythology"},{"id":21,"name":"Sports"},{"id":22,"name":"Geography"},{"id":23,"name":"History"},{"id":24,"name":"Politics"},{"id":25,"name":"Art"},{"id":26,"name":"Celebrities"},{"id":27,"name":"Animals"},{"id":28,"name":"Vehicles"},{"id":29,"name":"Entertainment:
    // Comics"},{"id":30,"name":"Science: Gadgets"},{"id":31,"name":"Entertainment: Japanese Anime &
    // Manga"},{"id":32,"name":"Entertainment: Cartoon & Animations"}]}

    private OpenDBResponse getDataFromOpenDB(@Max(50) Integer amount, Integer category, String difficulty) {
        return restTemplate.getForObject(
            "https://opentdb.com/api.php?amount={amount}&category={category}&difficulty={difficulty}",
            OpenDBResponse.class,
            amount,
            category,
            difficulty
        );
    }

    public List<Question> getQuizzFromOpenDB(Integer amount, Integer category, String difficulty) {
        OpenDBResponse data = getDataFromOpenDB(amount, category, difficulty);
        log.debug("data from OpenDB : {}", data);

        if (data == null || data.getResponseCode() > 0) {
            throw new RuntimeException(getMessageFromCode(data != null ? data.getResponseCode() : null));
        }

        return mapDataToQuestionList(data.getResults());
    }

    private List<Question> mapDataToQuestionList(List<Result> results) {
        List<Question> questions = new ArrayList<>();
        int index = 0;
        for (Result result : results) {
            Question question = new Question().statement(result.getQuestion()).index(++index);
            question.options(concatenateOptions(result.getIncorrectAnswers(), result.getCorrectAnswer()));
            question.setCorrectOptionIndex(getCorrectOptionIndex(question.getOptions(), result.getCorrectAnswer()));
            questions.add(question);
        }
        return questions;
    }

    private Integer getCorrectOptionIndex(Set<Option> options, String correctAnswer) {
        for (Option option : options) {
            if (option.getStatement().equals(correctAnswer)) {
                return option.getIndex();
            }
        }
        return 0;
    }

    private Set<Option> concatenateOptions(List<String> incorrectAnswers, String correctAnswer) {
        List<String> answers = new ArrayList<>(incorrectAnswers);
        Set<Option> options = new HashSet<>();
        answers.add(correctAnswer);
        Collections.shuffle(answers);
        int index = 0;
        for (String answer : answers) {
            options.add(new Option().statement(answer).index(++index));
        }
        return options;
    }

    private String getMessageFromCode(Integer responseCode) {
        // Code 0: Success Returned results successfully.

        switch (responseCode) {
            case 1:
                return "No Results Could not return results. The API doesn't have enough questions for your query. (Ex. Asking for 50 Questions in a Category that only has 20.)";
            case 2:
                return "Invalid Parameter Contains an invalid parameter. Arguements passed in aren't valid. (Ex. Amount = Five)";
            case 3:
                return "Token Not Found Session Token does not exist.";
            case 4:
                return "Token Empty Session Token has returned all possible questions for the specified query. Resetting the Token is necessary.";
            case 5:
                return "Rate Limit Too many requests have occurred. Each IP can only access the API once every 5 seconds.";
            default:
                break;
        }
        return "Unknown error";
    }
}
