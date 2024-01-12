package com.github.desprez.service.criteria;

import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import com.github.desprez.domain.enumeration.DisplayOrder;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.github.desprez.domain.Quizz} entity. This class is used
 * in {@link com.github.desprez.web.rest.QuizzResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quizzes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Difficulty
     */
    public static class DifficultyFilter extends Filter<Difficulty> {

        public DifficultyFilter() {}

        public DifficultyFilter(DifficultyFilter filter) {
            super(filter);
        }

        @Override
        public DifficultyFilter copy() {
            return new DifficultyFilter(this);
        }
    }

    /**
     * Class for filtering Category
     */
    public static class CategoryFilter extends Filter<Category> {

        public CategoryFilter() {}

        public CategoryFilter(CategoryFilter filter) {
            super(filter);
        }

        @Override
        public CategoryFilter copy() {
            return new CategoryFilter(this);
        }
    }

    /**
     * Class for filtering DisplayOrder
     */
    public static class DisplayOrderFilter extends Filter<DisplayOrder> {

        public DisplayOrderFilter() {}

        public DisplayOrderFilter(DisplayOrderFilter filter) {
            super(filter);
        }

        @Override
        public DisplayOrderFilter copy() {
            return new DisplayOrderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter title;

    private StringFilter description;

    private DifficultyFilter difficulty;

    private CategoryFilter category;

    private DisplayOrderFilter questionOrder;

    private IntegerFilter maxAnswerTime;

    private BooleanFilter allowBack;

    private BooleanFilter allowReview;

    private BooleanFilter secretGoodAnwers;

    private BooleanFilter published;

    private UUIDFilter questionsId;

    private LongFilter userId;

    private Boolean distinct;

    public QuizzCriteria() {}

    public QuizzCriteria(QuizzCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.difficulty = other.difficulty == null ? null : other.difficulty.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.questionOrder = other.questionOrder == null ? null : other.questionOrder.copy();
        this.maxAnswerTime = other.maxAnswerTime == null ? null : other.maxAnswerTime.copy();
        this.allowBack = other.allowBack == null ? null : other.allowBack.copy();
        this.allowReview = other.allowReview == null ? null : other.allowReview.copy();
        this.secretGoodAnwers = other.secretGoodAnwers == null ? null : other.secretGoodAnwers.copy();
        this.published = other.published == null ? null : other.published.copy();
        this.questionsId = other.questionsId == null ? null : other.questionsId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuizzCriteria copy() {
        return new QuizzCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DifficultyFilter getDifficulty() {
        return difficulty;
    }

    public DifficultyFilter difficulty() {
        if (difficulty == null) {
            difficulty = new DifficultyFilter();
        }
        return difficulty;
    }

    public void setDifficulty(DifficultyFilter difficulty) {
        this.difficulty = difficulty;
    }

    public CategoryFilter getCategory() {
        return category;
    }

    public CategoryFilter category() {
        if (category == null) {
            category = new CategoryFilter();
        }
        return category;
    }

    public void setCategory(CategoryFilter category) {
        this.category = category;
    }

    public DisplayOrderFilter getQuestionOrder() {
        return questionOrder;
    }

    public DisplayOrderFilter questionOrder() {
        if (questionOrder == null) {
            questionOrder = new DisplayOrderFilter();
        }
        return questionOrder;
    }

    public void setQuestionOrder(DisplayOrderFilter questionOrder) {
        this.questionOrder = questionOrder;
    }

    public IntegerFilter getMaxAnswerTime() {
        return maxAnswerTime;
    }

    public IntegerFilter maxAnswerTime() {
        if (maxAnswerTime == null) {
            maxAnswerTime = new IntegerFilter();
        }
        return maxAnswerTime;
    }

    public void setMaxAnswerTime(IntegerFilter maxAnswerTime) {
        this.maxAnswerTime = maxAnswerTime;
    }

    public BooleanFilter getAllowBack() {
        return allowBack;
    }

    public BooleanFilter allowBack() {
        if (allowBack == null) {
            allowBack = new BooleanFilter();
        }
        return allowBack;
    }

    public void setAllowBack(BooleanFilter allowBack) {
        this.allowBack = allowBack;
    }

    public BooleanFilter getAllowReview() {
        return allowReview;
    }

    public BooleanFilter allowReview() {
        if (allowReview == null) {
            allowReview = new BooleanFilter();
        }
        return allowReview;
    }

    public void setAllowReview(BooleanFilter allowReview) {
        this.allowReview = allowReview;
    }

    public BooleanFilter getSecretGoodAnwers() {
        return secretGoodAnwers;
    }

    public BooleanFilter secretGoodAnwers() {
        if (secretGoodAnwers == null) {
            secretGoodAnwers = new BooleanFilter();
        }
        return secretGoodAnwers;
    }

    public void setSecretGoodAnwers(BooleanFilter secretGoodAnwers) {
        this.secretGoodAnwers = secretGoodAnwers;
    }

    public BooleanFilter getPublished() {
        return published;
    }

    public BooleanFilter published() {
        if (published == null) {
            published = new BooleanFilter();
        }
        return published;
    }

    public void setPublished(BooleanFilter published) {
        this.published = published;
    }

    public UUIDFilter getQuestionsId() {
        return questionsId;
    }

    public UUIDFilter questionsId() {
        if (questionsId == null) {
            questionsId = new UUIDFilter();
        }
        return questionsId;
    }

    public void setQuestionsId(UUIDFilter questionsId) {
        this.questionsId = questionsId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuizzCriteria that = (QuizzCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(difficulty, that.difficulty) &&
            Objects.equals(category, that.category) &&
            Objects.equals(questionOrder, that.questionOrder) &&
            Objects.equals(maxAnswerTime, that.maxAnswerTime) &&
            Objects.equals(allowBack, that.allowBack) &&
            Objects.equals(allowReview, that.allowReview) &&
            Objects.equals(secretGoodAnwers, that.secretGoodAnwers) &&
            Objects.equals(published, that.published) &&
            Objects.equals(questionsId, that.questionsId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            difficulty,
            category,
            questionOrder,
            maxAnswerTime,
            allowBack,
            allowReview,
            secretGoodAnwers,
            published,
            questionsId,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (difficulty != null ? "difficulty=" + difficulty + ", " : "") +
            (category != null ? "category=" + category + ", " : "") +
            (questionOrder != null ? "questionOrder=" + questionOrder + ", " : "") +
            (maxAnswerTime != null ? "maxAnswerTime=" + maxAnswerTime + ", " : "") +
            (allowBack != null ? "allowBack=" + allowBack + ", " : "") +
            (allowReview != null ? "allowReview=" + allowReview + ", " : "") +
            (secretGoodAnwers != null ? "secretGoodAnwers=" + secretGoodAnwers + ", " : "") +
            (published != null ? "published=" + published + ", " : "") +
            (questionsId != null ? "questionsId=" + questionsId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
