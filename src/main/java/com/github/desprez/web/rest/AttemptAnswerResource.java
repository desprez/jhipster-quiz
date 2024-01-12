package com.github.desprez.web.rest;

import com.github.desprez.domain.AttemptAnswer;
import com.github.desprez.service.AttemptAnswerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.desprez.domain.AttemptAnswer}.
 */
@RestController
@RequestMapping("/api/attempt-answers")
public class AttemptAnswerResource {

    private final Logger log = LoggerFactory.getLogger(AttemptAnswerResource.class);

    private final AttemptAnswerService attemptAnswerService;

    public AttemptAnswerResource(AttemptAnswerService attemptAnswerService) {
        this.attemptAnswerService = attemptAnswerService;
    }

    /**
     * {@code GET  /attempt-answers} : get all the attemptAnswers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attemptAnswers in body.
     */
    @GetMapping("")
    public List<AttemptAnswer> getAllAttemptAnswers(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all AttemptAnswers");
        return attemptAnswerService.findAll();
    }

    /**
     * {@code GET  /attempt-answers/:id} : get the "id" attemptAnswer.
     *
     * @param id the id of the attemptAnswer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attemptAnswer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttemptAnswer> getAttemptAnswer(@PathVariable("id") UUID id) {
        log.debug("REST request to get AttemptAnswer : {}", id);
        Optional<AttemptAnswer> attemptAnswer = attemptAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attemptAnswer);
    }
}
