package com.github.desprez.service.mapper;

import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attempt} and its DTO {@link AttemptDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttemptMapper extends EntityMapper<AttemptDTO, Attempt> {
    @Mapping(target = "quizz", source = "quizz", qualifiedByName = "quizzTitle")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    AttemptDTO toDto(Attempt s);

    @Named("quizzTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    QuizzDTO toDtoQuizzTitle(Quizz quizz);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
