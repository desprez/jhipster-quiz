package com.github.desprez.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Attempt.
 */
@Entity
@Table(name = "attempt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Min(value = 0)
    @Column(name = "score", nullable = false)
    private Integer score;

    @NotNull
    @Column(name = "started", nullable = false)
    private Instant started;

    @Column(name = "ended")
    private Instant ended;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question", "option", "attempt" }, allowSetters = true)
    private Set<AttemptAnswer> answers = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "questions", "user" }, allowSetters = true)
    private Quizz quizz;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Attempt id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getScore() {
        return this.score;
    }

    public Attempt score(Integer score) {
        this.setScore(score);
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Instant getStarted() {
        return this.started;
    }

    public Attempt started(Instant started) {
        this.setStarted(started);
        return this;
    }

    public void setStarted(Instant started) {
        this.started = started;
    }

    public Instant getEnded() {
        return this.ended;
    }

    public Attempt ended(Instant ended) {
        this.setEnded(ended);
        return this;
    }

    public void setEnded(Instant ended) {
        this.ended = ended;
    }

    public Set<AttemptAnswer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(Set<AttemptAnswer> attemptAnswers) {
        if (this.answers != null) {
            this.answers.forEach(i -> i.setAttempt(null));
        }
        if (attemptAnswers != null) {
            attemptAnswers.forEach(i -> i.setAttempt(this));
        }
        this.answers = attemptAnswers;
    }

    public Attempt answers(Set<AttemptAnswer> attemptAnswers) {
        this.setAnswers(attemptAnswers);
        return this;
    }

    public Attempt addAnswers(AttemptAnswer attemptAnswer) {
        this.answers.add(attemptAnswer);
        attemptAnswer.setAttempt(this);
        return this;
    }

    public Attempt removeAnswers(AttemptAnswer attemptAnswer) {
        this.answers.remove(attemptAnswer);
        attemptAnswer.setAttempt(null);
        return this;
    }

    public Quizz getQuizz() {
        return this.quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

    public Attempt quizz(Quizz quizz) {
        this.setQuizz(quizz);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Attempt user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attempt)) {
            return false;
        }
        return getId() != null && getId().equals(((Attempt) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attempt{" +
            "id=" + getId() +
            ", score=" + getScore() +
            ", started='" + getStarted() + "'" +
            ", ended='" + getEnded() + "'" +
            "}";
    }
}
