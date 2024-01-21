package com.github.desprez.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AttemptAnswer.
 */
@Entity
@Table(name = "attempt_answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttemptAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    // @NotNull
    @Column(name = "started", nullable = false)
    private Instant started;

    @Column(name = "ended")
    private Instant ended;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "options", "quizz" }, allowSetters = true)
    private Question question;

    @ManyToOne(optional = false)
    // @NotNull
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "answers", "quizz", "user" }, allowSetters = true)
    private Attempt attempt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public AttemptAnswer id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Instant getStarted() {
        return this.started;
    }

    public AttemptAnswer started(Instant started) {
        this.setStarted(started);
        return this;
    }

    public void setStarted(Instant started) {
        this.started = started;
    }

    public Instant getEnded() {
        return this.ended;
    }

    public AttemptAnswer ended(Instant ended) {
        this.setEnded(ended);
        return this;
    }

    public void setEnded(Instant ended) {
        this.ended = ended;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public AttemptAnswer question(Question question) {
        this.setQuestion(question);
        return this;
    }

    public Option getOption() {
        return this.option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public AttemptAnswer option(Option option) {
        this.setOption(option);
        return this;
    }

    public Attempt getAttempt() {
        return this.attempt;
    }

    public void setAttempt(Attempt attempt) {
        this.attempt = attempt;
    }

    public AttemptAnswer attempt(Attempt attempt) {
        this.setAttempt(attempt);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttemptAnswer)) {
            return false;
        }
        return getId() != null && getId().equals(((AttemptAnswer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttemptAnswer{" +
            "id=" + getId() +
            ", started='" + getStarted() + "'" +
            ", ended='" + getEnded() + "'" +
            "}";
    }
}
