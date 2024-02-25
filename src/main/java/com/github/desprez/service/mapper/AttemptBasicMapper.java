package com.github.desprez.service.mapper;

import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.Quizz;
import com.github.desprez.domain.User;
import com.github.desprez.service.dto.AttemptBasicDTO;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.dto.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, componentModel = "spring")
public interface AttemptBasicMapper extends EntityMapper<AttemptBasicDTO, Attempt> {
    @Mapping(target = "quizz", source = "quizz", qualifiedByName = "quizzTitle")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    AttemptBasicDTO toDto(Attempt s);

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
