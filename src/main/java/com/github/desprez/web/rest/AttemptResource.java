package com.github.desprez.web.rest;

import com.github.desprez.repository.AttemptRepository;
import com.github.desprez.security.SecurityUtils;
import com.github.desprez.service.AttemptService;
import com.github.desprez.service.dto.AttemptDTO;
import com.github.desprez.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.desprez.domain.Attempt}.
 */
@RestController
@RequestMapping("/api/attempts")
public class AttemptResource {

    private final Logger log = LoggerFactory.getLogger(AttemptResource.class);

    public static final String ENTITY_NAME = "attempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttemptService attemptService;

    private final AttemptRepository attemptRepository;

    public AttemptResource(AttemptService attemptService, AttemptRepository attemptRepository) {
        this.attemptService = attemptService;
        this.attemptRepository = attemptRepository;
    }

    /**
     * {@code POST  /attempts} : Create a new attempt.
     *
     * @param attemptDTO the attemptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attemptDTO, or with status {@code 400 (Bad Request)} if the attempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AttemptDTO> createAttempt(@Valid @RequestBody AttemptDTO attemptDTO) throws URISyntaxException {
        log.debug("REST request to save Attempt : {}", attemptDTO);
        if (attemptDTO.getId() != null) {
            throw new BadRequestAlertException("A new attempt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttemptDTO result = attemptService.save(attemptDTO);
        return ResponseEntity
            .created(new URI("/api/attempts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attempts/:id} : Updates an existing attempt.
     *
     * @param id the id of the attemptDTO to save.
     * @param attemptDTO the attemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attemptDTO,
     * or with status {@code 400 (Bad Request)} if the attemptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AttemptDTO> updateAttempt(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AttemptDTO attemptDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Attempt : {}, {}", id, attemptDTO);
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
        AttemptDTO result = attemptService.update(attemptDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attemptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attempts/:id} : Partial updates given fields of an existing attempt, field will ignore if it is null
     *
     * @param id the id of the attemptDTO to save.
     * @param attemptDTO the attemptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attemptDTO,
     * or with status {@code 400 (Bad Request)} if the attemptDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attemptDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attemptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttemptDTO> partialUpdateAttempt(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AttemptDTO attemptDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attempt partially : {}, {}", id, attemptDTO);
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
        Optional<AttemptDTO> result = attemptService.partialUpdate(attemptDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attemptDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /attempts} : get all the attempts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attempts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AttemptDTO>> getAllAttempts(
        @ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Attempts");
        Page<AttemptDTO> page;
        if (eagerload) {
            page = attemptService.findAllWithEagerRelationships(pageable);
        } else {
            page = attemptService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /attempts/:id} : get the "id" attempt.
     *
     * @param id the id of the attemptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attemptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AttemptDTO> getAttempt(@PathVariable("id") UUID id) {
        log.debug("REST request to get Attempt : {}", id);
        Optional<AttemptDTO> attemptDTO = attemptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attemptDTO);
    }

    /**
     * {@code DELETE  /attempts/:id} : delete the "id" attempt.
     *
     * @param id the id of the attemptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttempt(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Attempt : {}", id);
        Optional<AttemptDTO> blog = attemptService.findOne(id);
        blog
            .filter(b -> b.getUser() != null && b.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse("")))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        attemptService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
