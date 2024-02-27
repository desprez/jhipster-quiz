package com.github.desprez.service.mapper;

import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.domain.Option;
import com.github.desprez.domain.Question;
import com.github.desprez.service.dto.AttemptAnswerDTO;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.dto.OptionDTO;
import com.github.desprez.service.dto.QuestionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link AttemptAnswer} and its DTO {@link AttemptAnswerDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttemptAnswerMapper extends EntityMapper<AttemptAnswerDTO, AttemptAnswer> {
    @Mapping(target = "question", source = "question", qualifiedByName = "questionStatement")
    @Mapping(target = "option", source = "option", qualifiedByName = "optionStatement")
    AttemptAnswerDTO toDto(AttemptAnswer s);

    @Named("questionStatement")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statement", source = "statement")
    QuestionDTO toDtoQuestionStatement(Question question);

    @Named("optionStatement")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "statement", source = "statement")
    OptionDTO toDtoOptionStatement(Option option);
}
