package com.github.desprez.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "statement", length = 255, nullable = false)
    private String statement;

    @NotNull
    @Column(name = "index", nullable = false)
    private Integer index;

    @NotNull
    @Column(name = "correct_option_index", nullable = false)
    private Integer correctOptionIndex;

    @OrderBy("index ASC")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "question" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questions", "user" }, allowSetters = true)
    private Quizz quizz;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Question id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatement() {
        return this.statement;
    }

    public Question statement(String statement) {
        this.setStatement(statement);
        return this;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getIndex() {
        return this.index;
    }

    public Question index(Integer index) {
        this.setIndex(index);
        return this;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getCorrectOptionIndex() {
        return this.correctOptionIndex;
    }

    public Question correctOptionIndex(Integer correctOptionIndex) {
        this.setCorrectOptionIndex(correctOptionIndex);
        return this;
    }

    public void setCorrectOptionIndex(Integer correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        if (this.options != null) {
            this.options.forEach(i -> i.setQuestion(null));
        }
        if (options != null) {
            options.forEach(i -> i.setQuestion(this));
        }
        this.options = options;
    }

    public Question options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public Question addOptions(Option option) {
        this.options.add(option);
        option.setQuestion(this);
        return this;
    }

    public Question removeOptions(Option option) {
        this.options.remove(option);
        option.setQuestion(null);
        return this;
    }

    public Quizz getQuizz() {
        return this.quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

    public Question quizz(Quizz quizz) {
        this.setQuizz(quizz);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return getId() != null && getId().equals(((Question) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", statement='" + getStatement() + "'" +
            ", index=" + getIndex() +
            ", correctOptionIndex=" + getCorrectOptionIndex() +
            "}";
    }
}
