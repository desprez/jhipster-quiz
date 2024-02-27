package com.github.desprez.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.NON_NULL)
public class AttemptDTO extends AttemptBasicDTO {

    private static final long serialVersionUID = 1L;

    private Set<AttemptAnswerDTO> answers = new HashSet<>();

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
            ", correctAnswerCount=" + getCorrectAnswerCount() +
            ", wrongAnswerCount=" + getWrongAnswerCount() +
            ", unansweredCount=" + getUnansweredCount() +
            ", started='" + getStarted() + "'" +
            ", ended='" + getEnded() + "'" +
            ", quizz=" + getQuizz() +
            ", user=" + getUser() +
            "}";
    }
}
