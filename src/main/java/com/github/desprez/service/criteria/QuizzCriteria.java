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

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter title;

    private StringFilter description;

    private DifficultyFilter difficulty;

    private CategoryFilter category;

    private IntegerFilter maxAnswerTime;
    private BooleanFilter published;
    private InstantFilter publishDate;
    private IntegerFilter questionCount;

    private LongFilter userId;

    private Boolean distinct;

    public QuizzCriteria() {}

    public QuizzCriteria(QuizzCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.difficulty = other.difficulty == null ? null : other.difficulty.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.maxAnswerTime = other.maxAnswerTime == null ? null : other.maxAnswerTime.copy();
        this.published = other.published == null ? null : other.published.copy();
        this.publishDate = other.publishDate == null ? null : other.publishDate.copy();
        this.questionCount = other.questionCount == null ? null : other.questionCount.copy();
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

    public InstantFilter getPublishDate() {
        return publishDate;
    }

    public InstantFilter publishDate() {
        if (publishDate == null) {
            publishDate = new InstantFilter();
        }
        return publishDate;
    }

    public void setPublishDate(InstantFilter publishDate) {
        this.publishDate = publishDate;
    }

    public IntegerFilter getQuestionCount() {
        return questionCount;
    }

    public IntegerFilter questionCount() {
        if (questionCount == null) {
            questionCount = new IntegerFilter();
        }
        return questionCount;
    }

    public void setQuestionCount(IntegerFilter questionCount) {
        this.questionCount = questionCount;
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
            Objects.equals(maxAnswerTime, that.maxAnswerTime) &&
            Objects.equals(published, that.published) &&
            Objects.equals(publishDate, that.publishDate) &&
            Objects.equals(questionCount, that.questionCount) &&
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
            maxAnswerTime,
            published,
            publishDate,
            questionCount,
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
			(maxAnswerTime != null ? "maxAnswerTime=" + maxAnswerTime + ", " : "") +
            (published != null ? "published=" + published + ", " : "") +
			(publishDate != null ? "publishDate=" + publishDate + ", " : "") +
            (questionCount != null ? "questionCount=" + questionCount + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
