package com.github.desprez.web.rest;

import com.github.desprez.domain.Option;
import com.github.desprez.service.OptionService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.desprez.domain.Option}.
 */
@RestController
@RequestMapping("/api/options")
public class OptionResource {

    private final Logger log = LoggerFactory.getLogger(OptionResource.class);

    private final OptionService optionService;

    public OptionResource(OptionService optionService) {
        this.optionService = optionService;
    }

    /**
     * {@code GET  /options} : get all the options.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of options in body.
     */
    @GetMapping("")
    public List<Option> getAllOptions() {
        log.debug("REST request to get all Options");
        return optionService.findAll();
    }

    /**
     * {@code GET  /options/:id} : get the "id" option.
     *
     * @param id the id of the option to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the option, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Option> getOption(@PathVariable("id") UUID id) {
        log.debug("REST request to get Option : {}", id);
        Optional<Option> option = optionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(option);
    }
}
