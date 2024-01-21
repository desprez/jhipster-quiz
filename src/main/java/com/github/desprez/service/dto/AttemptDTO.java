package com.github.desprez.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.github.desprez.domain.Attempt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttemptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    @Min(value = 0)
    private Integer score;

    @NotNull
    private Instant started;

    private Instant ended;

    private QuizzDTO quizz;

    private UserDTO user;

    private Set<AttemptAnswerDTO> answers = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Instant getStarted() {
        return started;
    }

    public void setStarted(Instant started) {
        this.started = started;
    }

    public Instant getEnded() {
        return ended;
    }

    public void setEnded(Instant ended) {
        this.ended = ended;
    }

    public QuizzDTO getQuizz() {
        return quizz;
    }

    public void setQuizz(QuizzDTO quizz) {
        this.quizz = quizz;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<AttemptAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<AttemptAnswerDTO> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttemptDTO)) {
            return false;
        }

        AttemptDTO attemptDTO = (AttemptDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attemptDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttemptDTO{" +
            "id='" + getId() + "'" +
            ", score=" + getScore() +
            ", started='" + getStarted() + "'" +
            ", ended='" + getEnded() + "'" +
            ", quizz=" + getQuizz() +
            ", user=" + getUser() +
            "}";
    }
}
