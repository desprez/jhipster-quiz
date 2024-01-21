package com.github.desprez.service.dto;

import com.github.desprez.domain.enumeration.DisplayOrder;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.github.desprez.domain.Quizz} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzDTO extends QuizzBasicDTO {

    @NotNull
    private DisplayOrder questionOrder;

    private Integer maxAnswerTime;

    @NotNull
    private Boolean allowBack;

    @NotNull
    private Boolean allowReview;

    @NotNull
    private Boolean secretGoodAnwers;

    @Lob
    private byte[] image;

    private String imageContentType;

    private Set<QuestionDTO> questions = new HashSet<>();

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

    public Boolean getAllowBack() {
        return allowBack;
    }

    public void setAllowBack(Boolean allowBack) {
        this.allowBack = allowBack;
    }

    public Boolean getAllowReview() {
        return allowReview;
    }

    public void setAllowReview(Boolean allowReview) {
        this.allowReview = allowReview;
    }

    public Boolean getSecretGoodAnwers() {
        return secretGoodAnwers;
    }

    public void setSecretGoodAnwers(Boolean secretGoodAnwers) {
        this.secretGoodAnwers = secretGoodAnwers;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuestionDTO> questions) {
        this.questions = questions;
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
            ", questionOrder='" + getQuestionOrder() + "'" +
            ", maxAnswerTime=" + getMaxAnswerTime() +
            ", allowBack='" + getAllowBack() + "'" +
            ", allowReview='" + getAllowReview() + "'" +
            ", secretGoodAnwers='" + getSecretGoodAnwers() + "'" +
            ", image='" + getImage() + "'" +
            ", published='" + getPublished() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
