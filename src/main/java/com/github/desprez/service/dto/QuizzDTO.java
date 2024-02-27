package com.github.desprez.service.dto;

import com.github.desprez.domain.enumeration.DisplayOrder;
import com.github.desprez.domain.enumeration.Period;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.github.desprez.domain.Quizz} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
//@JsonInclude(Include.NON_EMPTY)
public class QuizzDTO extends QuizzBasicDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    private DisplayOrder questionOrder;

    private Integer maxAnswerTime;

    @NotNull
    private Boolean allowBack;

    @NotNull
    private Boolean allowReview;

    @NotNull
    private Boolean keepAnswersSecret;

    private Integer attempsLimit;

    private Period attempsLimitPeriod;

    private List<QuestionDTO> questions = new ArrayList<>();

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

    public Boolean getKeepAnswersSecret() {
        return keepAnswersSecret;
    }

    public void setKeepAnswersSecret(Boolean keepAnswersSecret) {
        this.keepAnswersSecret = keepAnswersSecret;
    }

    public Integer getAttempsLimit() {
        return attempsLimit;
    }

    public void setAttempsLimit(Integer attempsLimit) {
        this.attempsLimit = attempsLimit;
    }

    public Period getAttempsLimitPeriod() {
        return attempsLimitPeriod;
    }

    public void setAttempsLimitPeriod(Period attempsLimitPeriod) {
        this.attempsLimitPeriod = attempsLimitPeriod;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
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
            ", keepAnswersSecret='" + getKeepAnswersSecret() + "'" +
            ", published='" + getPublished() + "'" +
            ", attempsLimit=" + getAttempsLimit() +
            ", attempsLimitPeriod='" + getAttempsLimitPeriod() + "'" +
            ", questions='" + getQuestions() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
