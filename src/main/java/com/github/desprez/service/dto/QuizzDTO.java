package com.github.desprez.service.dto;

import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import com.github.desprez.domain.enumeration.DisplayOrder;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.github.desprez.domain.Quizz} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzDTO implements Serializable {

    private UUID id;

    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Category category;

    @NotNull
    private Boolean published;

    @NotNull
    private DisplayOrder questionOrder;

    @NotNull
    private Integer maxAnswerTime;

    @NotNull
    private Boolean rollbackAllowed;

    private UserDTO user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public DisplayOrder getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(DisplayOrder questionOrder) {
        this.questionOrder = questionOrder;
    }

    public Integer getMaxAnswerTime() {
        return maxAnswerTime;
    }

    public void setMaxAnswerTime(Integer maxAnswerTime) {
        this.maxAnswerTime = maxAnswerTime;
    }

    public Boolean getRollbackAllowed() {
        return rollbackAllowed;
    }

    public void setRollbackAllowed(Boolean rollbackAllowed) {
        this.rollbackAllowed = rollbackAllowed;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizzDTO)) {
            return false;
        }

        QuizzDTO quizzDTO = (QuizzDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizzDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", difficulty='" + getDifficulty() + "'" +
            ", category='" + getCategory() + "'" +
            ", published='" + getPublished() + "'" +
            ", questionOrder='" + getQuestionOrder() + "'" +
            ", maxAnswerTime=" + getMaxAnswerTime() +
            ", rollbackAllowed='" + getRollbackAllowed() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
