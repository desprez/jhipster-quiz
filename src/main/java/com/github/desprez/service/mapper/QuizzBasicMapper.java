package com.github.desprez.service.mapper;

import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.service.dto.QuizzBasicDTO;
import com.github.desprez.service.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Quizz} and its Light DTO {@link QuizzBasicDTO}.
 */
@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, componentModel = "spring")
@DecoratedWith(QuizzMapperDecorator.class)
public interface QuizzBasicMapper extends EntityMapper<QuizzBasicDTO, Quizz> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    QuizzBasicDTO toDto(Quizz s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
