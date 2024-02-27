package com.github.desprez.service.impl;

import com.github.desprez.domain.Quizz;
import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.repository.UserRepository;
import com.github.desprez.security.AuthoritiesConstants;
import com.github.desprez.security.SecurityUtils;
import com.github.desprez.service.QuizzService;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.service.mapper.QuizzMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.desprez.domain.Quizz}.
 */
@Service
@Transactional
public class QuizzServiceImpl implements QuizzService {

    private final Logger log = LoggerFactory.getLogger(QuizzServiceImpl.class);

    protected final QuizzRepository quizzRepository;

    protected final QuizzMapper quizzMapper;

    private final UserRepository userRepository;

    public QuizzServiceImpl(QuizzRepository quizzRepository, QuizzMapper quizzMapper, UserRepository userRepository) {
        this.quizzRepository = quizzRepository;
        this.quizzMapper = quizzMapper;
        this.userRepository = userRepository;
    }

    @Override
    public QuizzDTO save(QuizzDTO quizzDTO) {
        log.debug("Request to save Quizz : {}", quizzDTO);
        Quizz quizz = quizzMapper.toEntity(quizzDTO);
        if (!SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            String username = getUsername();
            quizz.setUser(userRepository.findOneByLogin(username).orElseThrow());
        }
        quizz.setQuestionCount(quizz.getQuestions().size());
        quizz = quizzRepository.save(quizz);
        return quizzMapper.toDto(quizz);
    }

    private String getUsername() {
        String username = SecurityUtils.getCurrentUserLogin().orElseThrow();
        log.debug("No user passed in, using current user: {}", username);
        return username;
    }

    @Override
    public QuizzDTO update(QuizzDTO quizzDTO) {
        log.debug("Request to update Quizz : {}", quizzDTO);
        Quizz quizz = quizzMapper.toEntity(quizzDTO);
        quizz.setQuestionCount(quizz.getQuestions().size());
        quizz = quizzRepository.save(quizz);
        return quizzMapper.toDto(quizz);
    }

    @Override
    public Optional<QuizzDTO> partialUpdate(QuizzDTO quizzDTO) {
        log.debug("Request to partially update Quizz : {}", quizzDTO);

        return quizzRepository
            .findById(quizzDTO.getId())
            .map(existingQuizz -> {
                quizzMapper.partialUpdate(existingQuizz, quizzDTO);

                return existingQuizz;
            })
            .map(quizzRepository::save)
            .map(quizzMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuizzDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Quizzes");
        return quizzRepository.findAll(pageable).map(quizzMapper::toDto);
    }

    public Page<QuizzDTO> findAllWithEagerRelationships(Pageable pageable) {
        return quizzRepository.findAllWithEagerRelationships(pageable).map(quizzMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QuizzDTO> findOne(UUID id) {
        log.debug("Request to get Quizz : {}", id);
        return quizzRepository.findOneWithQuestionRelationships(id).map(quizzMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Quizz : {}", id);
        quizzRepository.deleteById(id);
    }
}
