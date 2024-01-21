package com.github.desprez.service.impl;

import com.github.desprez.domain.Option;
import com.github.desprez.repository.OptionRepository;
import com.github.desprez.service.OptionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.desprez.domain.Option}.
 */
@Service
@Transactional
public class OptionServiceImpl implements OptionService {

    private final Logger log = LoggerFactory.getLogger(OptionServiceImpl.class);

    private final OptionRepository optionRepository;

    public OptionServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public Option save(Option option) {
        log.debug("Request to save Option : {}", option);
        return optionRepository.save(option);
    }

    @Override
    public Option update(Option option) {
        log.debug("Request to update Option : {}", option);
        return optionRepository.save(option);
    }

    @Override
    public Optional<Option> partialUpdate(Option option) {
        log.debug("Request to partially update Option : {}", option);

        return optionRepository
            .findById(option.getId())
            .map(existingOption -> {
                if (option.getStatement() != null) {
                    existingOption.setStatement(option.getStatement());
                }
                if (option.getIndex() != null) {
                    existingOption.setIndex(option.getIndex());
                }

                return existingOption;
            })
            .map(optionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Option> findAll() {
        log.debug("Request to get all Options");
        return optionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Option> findOne(UUID id) {
        log.debug("Request to get Option : {}", id);
        return optionRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}
