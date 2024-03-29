package com.github.desprez.service.impl;

import com.github.desprez.domain.Attempt;
import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.domain.Question;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.repository.QuestionRepository;
import com.github.desprez.repository.UserRepository;
import com.github.desprez.security.SecurityUtils;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.mapper.AttemptMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Primary
@Service
@Transactional
public class AttemptServiceExtendedImpl extends AttemptServiceImpl {

    private final Logger log = LoggerFactory.getLogger(AttemptServiceImpl.class);
    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AttemptMapper attemptMapper;

    public AttemptServiceExtendedImpl(
        AttemptRepository attemptRepository,
        AttemptMapper attemptMapper,
        UserRepository userRepository,
        QuestionRepository questionRepository
    ) {
        super(attemptRepository, attemptMapper);
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
        this.attemptMapper = attemptMapper;
        this.userRepository = userRepository;
    }

    @Override
    public AttemptDTO save(AttemptDTO attemptDTO) {
        log.debug("Request to save Attempt : {}", attemptDTO);
        Attempt attempt = attemptMapper.toEntity(attemptDTO);

        log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin().get());
        String username = SecurityUtils.getCurrentUserLogin().get();
        attempt.setUser(userRepository.findOneByLogin(username).get());

        List<Question> questions = questionRepository.findByQuizzId(attempt.getQuizz().getId());

        questions.forEach(question -> {
            AttemptAnswer attemptAnswer = new AttemptAnswer().attempt(attempt).question(question);
            attempt.getAnswers().add(attemptAnswer);
        });

        return attemptMapper.toDto(attemptRepository.save(attempt));
    }
}
