package com.github.desprez.web.rest;

import com.github.desprez.repository.QuizzRepository;
import com.github.desprez.service.QuizzQueryService;
import com.github.desprez.service.QuizzServiceExtended;
import com.github.desprez.service.criteria.QuizzCriteria;
import com.github.desprez.service.dto.QuizzBasicDTO;
import com.github.desprez.service.dto.QuizzDTO;
import com.github.desprez.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.desprez.domain.Quizz}.
 */
@RestController
@RequestMapping("/api/quizzes")
public class QuizzResource {

    private final Logger log = LoggerFactory.getLogger(QuizzResource.class);

    private static final String ENTITY_NAME = "quizz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizzServiceExtended quizzService;

    private final QuizzRepository quizzRepository;

    private final QuizzQueryService quizzQueryService;

    public QuizzResource(QuizzServiceExtended quizzService, QuizzRepository quizzRepository, QuizzQueryService quizzQueryService) {
        this.quizzService = quizzService;
        this.quizzRepository = quizzRepository;
        this.quizzQueryService = quizzQueryService;
    }

    /**
     * {@code POST  /quizzes} : Create a new quizz.
     *
     * @param quizzDTO the quizzDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizzDTO, or with status {@code 400 (Bad Request)} if the quizz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QuizzDTO> createQuizz(@Valid @RequestBody QuizzDTO quizzDTO) throws URISyntaxException {
        log.debug("REST request to save Quizz : {}", quizzDTO);
        if (quizzDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizzDTO result = quizzService.save(quizzDTO);
        return ResponseEntity
            .created(new URI("/api/quizzes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quizzes/:id} : Updates an existing quizz.
     *
     * @param id the id of the quizzDTO to save.
     * @param quizzDTO the quizzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzDTO,
     * or with status {@code 400 (Bad Request)} if the quizzDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizzDTO> updateQuizz(
        @Parameter(description = "id of quizz to be updated", example = "7bf9fa79-5b46-4bb0-bb38-298bf9bd036b") @PathVariable(
            value = "id",
            required = false
        ) final UUID id,
        @Valid @RequestBody QuizzDTO quizzDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Quizz : {}, {}", id, quizzDTO);
        if (quizzDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizzDTO result = quizzService.update(quizzDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quizzes/:id} : Partial updates given fields of an existing quizz, field will ignore if it is null
     *
     * @param id the id of the quizzDTO to save.
     * @param quizzDTO the quizzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzDTO,
     * or with status {@code 400 (Bad Request)} if the quizzDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizzDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizzDTO> partialUpdateQuizz(
        @Parameter(description = "id of quizz to be updated", example = "7bf9fa79-5b46-4bb0-bb38-298bf9bd036b") @PathVariable(
            value = "id",
            required = false
        ) final UUID id,
        @NotNull @RequestBody QuizzDTO quizzDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quizz partially : {}, {}", id, quizzDTO);
        if (quizzDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizzDTO> result = quizzService.partialUpdate(quizzDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quizzes} : get all the quizzes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QuizzBasicDTO>> getAllQuizzes(QuizzCriteria criteria, @ParameterObject Pageable pageable) {
        log.debug("REST request to get Quizzes by criteria: {}", criteria);

        Page<QuizzBasicDTO> page = quizzQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quizzes/count} : count all the quizzes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQuizzes(QuizzCriteria criteria) {
        log.debug("REST request to count Quizzes by criteria: {}", criteria);
        return ResponseEntity.ok().body(quizzQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /quizzes/:id} : get the "id" quizz.
     *
     * @param id the id of the quizzDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizzDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizzDTO> getQuizz(
        @Parameter(description = "id of quizz to be retrieved", example = "7bf9fa79-5b46-4bb0-bb38-298bf9bd036b") @PathVariable(
            "id"
        ) UUID id
    ) {
        log.debug("REST request to get Quizz : {}", id);
        Optional<QuizzDTO> quizzDTO = quizzService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizzDTO);
    }

    /**
     * {@code DELETE  /quizzes/:id} : delete the "id" quizz.
     *
     * @param id the id of the quizzDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizz(
        @Parameter(description = "id of quizz to be deleted", example = "7bf9fa79-5b46-4bb0-bb38-298bf9bd036b") @PathVariable("id") UUID id
    ) {
        log.debug("REST request to delete Quizz : {}", id);
        quizzService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PUBLISH  /quizzes/:id/publish} : publish the "id" quizz.
     *
     * @param id the id of the quizzDTO to publish.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PutMapping("/{id}/publish")
    public ResponseEntity<Void> publishQuizz(
        @Parameter(description = "id of quizz to be published", example = "7bf9fa79-5b46-4bb0-bb38-298bf9bd036b") @PathVariable(
            "id"
        ) UUID id
    ) {
        log.debug("REST request to publish Quizz : {}", id);
        quizzService.publishQuiz(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
