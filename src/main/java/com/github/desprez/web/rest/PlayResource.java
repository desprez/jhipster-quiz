package com.github.desprez.web.rest;

import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.security.SecurityUtils;
import com.github.desprez.service.PlayService;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/play")
public class PlayResource {

    private final Logger log = LoggerFactory.getLogger(PlayResource.class);

    public static final String ENTITY_NAME = "attempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private PlayService playService;

    private AttemptRepository attemptRepository;

    public PlayResource(PlayService playService, AttemptRepository attemptRepository) {
        this.playService = playService;
        this.attemptRepository = attemptRepository;
    }

    /**
     * {@code GET  /play/:id} : play the "id" quizz.
     *
     * @param id the id of the quizz to play.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the AttemptDTO,
     *         or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttemptDTO> play(
        @Parameter(description = "id of quizz to be played", example = "7bf9fa79-5b46-4bb0-bb38-298bf9bd036b") @PathVariable(
            "id"
        ) UUID quizId
    ) throws URISyntaxException {
        log.debug("REST request to get Quizz : {}", quizId);

        AttemptDTO result = playService.start(quizId);

        return ResponseEntity
            .created(new URI("/api/attempts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, AttemptResource.ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/evaluate/{id}")
    public ResponseEntity<AttemptDTO> evaluate(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AttemptDTO attemptDTO
    ) {
        log.debug("REST request to evaluate Attempt : {}, {}", id, attemptDTO);
        if (attemptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attemptDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!attemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (attemptDTO.getUser() != null && !attemptDTO.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        AttemptDTO result = playService.evaluate(attemptDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attemptDTO.getId().toString()))
            .body(result);
    }
}
