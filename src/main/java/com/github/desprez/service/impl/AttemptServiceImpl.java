package com.github.desprez.service.impl;

import com.github.desprez.domain.Attempt;
import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.service.AttemptService;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.service.mapper.AttemptMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.desprez.domain.Attempt}.
 */
@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final Logger log = LoggerFactory.getLogger(AttemptServiceImpl.class);

    private final AttemptRepository attemptRepository;

    private final AttemptMapper attemptMapper;

    public AttemptServiceImpl(AttemptRepository attemptRepository, AttemptMapper attemptMapper) {
        this.attemptRepository = attemptRepository;
        this.attemptMapper = attemptMapper;
    }

    @Override
    public AttemptDTO save(AttemptDTO attemptDTO) {
        log.debug("Request to save Attempt : {}", attemptDTO);
        Attempt attempt = attemptMapper.toEntity(attemptDTO);
        attempt = attemptRepository.save(attempt);
        return attemptMapper.toDto(attempt);
    }

    @Override
    public AttemptDTO update(AttemptDTO attemptDTO) {
        log.debug("Request to update Attempt : {}", attemptDTO);
        Attempt attempt = attemptMapper.toEntity(attemptDTO);
        attempt = attemptRepository.save(attempt);
        return attemptMapper.toDto(attempt);
    }

    @Override
    public Optional<AttemptDTO> partialUpdate(AttemptDTO attemptDTO) {
        log.debug("Request to partially update Attempt : {}", attemptDTO);

        return attemptRepository
            .findById(attemptDTO.getId())
            .map(existingAttempt -> {
                attemptMapper.partialUpdate(existingAttempt, attemptDTO);

                return existingAttempt;
            })
            .map(attemptRepository::save)
            .map(attemptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AttemptDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attempts");
        return attemptRepository.findAll(pageable).map(attemptMapper::toDto);
    }

    public Page<AttemptDTO> findAllWithEagerRelationships(Pageable pageable) {
        return attemptRepository.findAllWithEagerRelationships(pageable).map(attemptMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AttemptDTO> findOne(UUID id) {
        log.debug("Request to get Attempt : {}", id);
        return attemptRepository.findOneWithEagerRelationships(id).map(attemptMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Attempt : {}", id);
        attemptRepository.deleteById(id);
    }
}
