package com.github.desprez.service.mapper;

import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quizz} and its DTO {@link QuizzDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuizzMapper extends EntityMapper<QuizzDTO, Quizz> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    QuizzDTO toDto(Quizz s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
