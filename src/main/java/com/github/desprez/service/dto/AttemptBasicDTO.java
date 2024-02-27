package com.github.desprez.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.github.desprez.domain.Attempt} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@JsonInclude(Include.NON_NULL)
public class AttemptBasicDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    @Min(value = 0)
    private Integer correctAnswerCount;

    @NotNull
    @Min(value = 0)
    private Integer wrongAnswerCount;

    @NotNull
    @Min(value = 0)
    private Integer unansweredCount;

    @NotNull
    private Instant started;

    private Instant ended;

    private QuizzDTO quizz;

    private UserDTO user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getCorrectAnswerCount() {
        return correctAnswerCount;
    }

    public void setCorrectAnswerCount(Integer correctAnswerCount) {
        this.correctAnswerCount = correctAnswerCount;
    }

    public AttemptBasicDTO correctAnswerCount(Integer correctAnswerCount) {
        this.correctAnswerCount = correctAnswerCount;
        return this;
    }

    public Integer getWrongAnswerCount() {
        return wrongAnswerCount;
    }

    public void setWrongAnswerCount(Integer wrongAnswerCount) {
        this.wrongAnswerCount = wrongAnswerCount;
    }

    public AttemptBasicDTO wrongAnswerCount(Integer wrongAnswerCount) {
        this.wrongAnswerCount = correctAnswerCount;
        return this;
    }

    public Integer getUnansweredCount() {
        return unansweredCount;
    }

    public void setUnansweredCount(Integer unansweredCount) {
        this.unansweredCount = unansweredCount;
    }

    public AttemptBasicDTO unansweredCount(Integer unansweredCount) {
        this.unansweredCount = unansweredCount;
        return this;
    }

    public Instant getStarted() {
        return started;
    }

    public void setStarted(Instant started) {
        this.started = started;
    }

    public AttemptBasicDTO started(Instant started) {
        this.started = started;
        return this;
    }

    public Instant getEnded() {
        return ended;
    }

    public void setEnded(Instant ended) {
        this.ended = ended;
    }

    public AttemptBasicDTO ended(Instant ended) {
        this.ended = ended;
        return this;
    }

    public QuizzDTO getQuizz() {
        return quizz;
    }

    public void setQuizz(QuizzDTO quizz) {
        this.quizz = quizz;
    }

    public AttemptBasicDTO quizz(QuizzDTO quizz) {
        this.quizz = quizz;
        return this;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AttemptBasicDTO user(UserDTO user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttemptBasicDTO)) {
            return false;
        }

        AttemptBasicDTO attemptBasicDTO = (AttemptBasicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attemptBasicDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "AttemptDTO{" + "id='" + getId() + "'" 
        + ", correctAnswerCount=" + getCorrectAnswerCount() 
        + ", wrongAnswerCount=" + getWrongAnswerCount()
        + ", unansweredCount=" + getUnansweredCount() 
        + ", started='" + getStarted() + "'"
        + ", ended='" + getEnded() + "'" 
        + ", quizz=" + getQuizz() 
        + ", user=" + getUser() + "}";
  }
}
