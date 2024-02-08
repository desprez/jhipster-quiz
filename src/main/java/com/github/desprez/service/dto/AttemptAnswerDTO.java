package com.github.desprez.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(Include.NON_EMPTY)
public class AttemptAnswerDTO {

    private UUID id;

    private Instant started;

    private Instant ended;

    private QuestionDTO question;

    private OptionDTO option;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public QuestionDTO getQuestion() {
        return question;
    }

    public void setQuestion(QuestionDTO question) {
        this.question = question;
    }

    public OptionDTO getOption() {
        return option;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttemptAnswerDTO)) {
            return false;
        }

        AttemptAnswerDTO attemptAnswerDTO = (AttemptAnswerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attemptAnswerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
  @Override
  public String toString() {
    return "AttemptAnswerDTO{" + "id='" + getId() + "'" + ", started='" + getStarted() + "'"
        + ", ended='" + getEnded() + "'" + ", question='" + getQuestion() + "'" + ", option='"
        + getOption() + "'" + "}";
  }
}
