package com.github.desprez.service.mapper;

import com.github.desprez.domain.Quizz;
import com.github.desprez.service.QuestionService;
import com.github.desprez.service.dto.QuizzBasicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class QuizzMapperDecorator implements QuizzBasicMapper {

    @Autowired
    @Qualifier("delegate")
    private QuizzBasicMapper delegate;

    @Autowired
    private QuestionService questionService;

    @Override
    public QuizzBasicDTO toDto(Quizz quizz) {
        QuizzBasicDTO quizzDTO = delegate.toDto(quizz);

        quizzDTO.setQuestionCount(questionService.countByQuizzId(quizz.getId()));

        return quizzDTO;
    }
}
