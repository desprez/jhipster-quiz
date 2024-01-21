package com.github.desprez.service.dto;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class AttemptAnswerDTO {

    private UUID id;

    private Instant started;

    private Instant ended;

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
    return "AttemptAnswerDTO{" + "id='" + getId() + "'" 
       + ", started='" + getStarted() + "'"  
       + ", ended='" + getEnded() + "'"  
       + ", option='" + getOption() + "'" + "}";
  }
}
