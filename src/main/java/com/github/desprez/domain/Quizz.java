package com.github.desprez.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import com.github.desprez.domain.enumeration.DisplayOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quizz.
 */
@Entity
@Table(name = "quizz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quizz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @NotNull
    @Column(name = "published", nullable = false)
    private Boolean published;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "question_order", nullable = false)
    private DisplayOrder questionOrder;

    @NotNull
    @Column(name = "max_answer_time", nullable = false)
    private Integer maxAnswerTime;

    @NotNull
    @Column(name = "rollback_allowed", nullable = false)
    private Boolean rollbackAllowed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quizz")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "options", "quizz" }, allowSetters = true)
    private Set<Question> questions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Quizz id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Quizz title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Quizz description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public Quizz difficulty(Difficulty difficulty) {
        this.setDifficulty(difficulty);
        return this;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Category getCategory() {
        return this.category;
    }

    public Quizz category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getPublished() {
        return this.published;
    }

    public Quizz published(Boolean published) {
        this.setPublished(published);
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public DisplayOrder getQuestionOrder() {
        return this.questionOrder;
    }

    public Quizz questionOrder(DisplayOrder questionOrder) {
        this.setQuestionOrder(questionOrder);
        return this;
    }

    public void setQuestionOrder(DisplayOrder questionOrder) {
        this.questionOrder = questionOrder;
    }

    public Integer getMaxAnswerTime() {
        return this.maxAnswerTime;
    }

    public Quizz maxAnswerTime(Integer maxAnswerTime) {
        this.setMaxAnswerTime(maxAnswerTime);
        return this;
    }

    public void setMaxAnswerTime(Integer maxAnswerTime) {
        this.maxAnswerTime = maxAnswerTime;
    }

    public Boolean getRollbackAllowed() {
        return this.rollbackAllowed;
    }

    public Quizz rollbackAllowed(Boolean rollbackAllowed) {
        this.setRollbackAllowed(rollbackAllowed);
        return this;
    }

    public void setRollbackAllowed(Boolean rollbackAllowed) {
        this.rollbackAllowed = rollbackAllowed;
    }

    public Set<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(Set<Question> questions) {
        if (this.questions != null) {
            this.questions.forEach(i -> i.setQuizz(null));
        }
        if (questions != null) {
            questions.forEach(i -> i.setQuizz(this));
        }
        this.questions = questions;
    }

    public Quizz questions(Set<Question> questions) {
        this.setQuestions(questions);
        return this;
    }

    public Quizz addQuestions(Question question) {
        this.questions.add(question);
        question.setQuizz(this);
        return this;
    }

    public Quizz removeQuestions(Question question) {
        this.questions.remove(question);
        question.setQuizz(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quizz user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quizz)) {
            return false;
        }
        return getId() != null && getId().equals(((Quizz) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quizz{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            ", category='" + getCategory() + "'" +
            ", published='" + getPublished() + "'" +
            ", questionOrder='" + getQuestionOrder() + "'" +
            ", maxAnswerTime=" + getMaxAnswerTime() +
            ", rollbackAllowed='" + getRollbackAllowed() + "'" +
            "}";
    }
}
