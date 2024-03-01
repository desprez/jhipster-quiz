package com.github.desprez.service.mapper;

import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.domain.enumeration.DisplayOrder;
import com.github.desprez.service.dto.QuestionDTO;
import com.github.desprez.service.dto.QuizzBasicDTO;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.dto.UserDTO;
import java.util.Collections;
import java.util.Comparator;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Quizz} and its DTO {@link QuizzDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizzMapper extends EntityMapper<QuizzDTO, Quizz> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    QuizzDTO toDto(Quizz s);

    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    QuizzBasicDTO toBasicDto(Quizz s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @AfterMapping
    default void map(Quizz entity, @MappingTarget QuizzDTO target) {
        if (DisplayOrder.FIXED.equals(target.getQuestionOrder())) {
            Collections.sort(
                target.getQuestions(),
                new Comparator<QuestionDTO>() {
                    @Override
                    public int compare(QuestionDTO b1, QuestionDTO b2) {
                        return b1.getIndex().compareTo(b2.getIndex());
                    }
                }
            );
        }
        if (DisplayOrder.RANDOM.equals(target.getQuestionOrder())) {
            Collections.shuffle(target.getQuestions());
        }
    }
}
