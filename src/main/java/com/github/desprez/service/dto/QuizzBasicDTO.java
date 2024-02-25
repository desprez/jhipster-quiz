package com.github.desprez.service.dto;

import com.github.desprez.domain.enumeration.Category;
import com.github.desprez.domain.enumeration.Difficulty;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class QuizzBasicDTO {

    private static final long serialVersionUID = 1L;

    protected UUID id;

    @NotNull
    @Size(min = 2, max = 100)
    protected String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private Difficulty difficulty;

    @NotNull
    private Category category;

    @NotNull
    private Boolean published;

    private UserDTO user;

    @Lob
    private byte[] image;

    private String imageContentType;

    private Integer questionCount;

    private Instant publishDate;

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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    // @JsonGetter
    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Instant getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizzBasicDTO)) {
            return false;
        }

        QuizzBasicDTO quizzBasicDTO = (QuizzBasicDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quizzBasicDTO.id);
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
          ", image='" + getImage() + "'" +
          ", user=" + getUser() +
          "}";
  }
}
