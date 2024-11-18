package smallpocs.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import smallpocs.repository.EffectifRepository;
import smallpocs.service.EffectifService;
import smallpocs.service.dto.EffectifDTO;
import smallpocs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link smallpocs.domain.Effectif}.
 */
@RestController
@RequestMapping("/api/effectifs")
public class EffectifResource {

    private static final Logger LOG = LoggerFactory.getLogger(EffectifResource.class);

    private static final String ENTITY_NAME = "effectif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EffectifService effectifService;

    private final EffectifRepository effectifRepository;

    public EffectifResource(EffectifService effectifService, EffectifRepository effectifRepository) {
        this.effectifService = effectifService;
        this.effectifRepository = effectifRepository;
    }

    /**
     * {@code POST  /effectifs} : Create a new effectif.
     *
     * @param effectifDTO the effectifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new effectifDTO, or with status {@code 400 (Bad Request)} if the effectif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EffectifDTO> createEffectif(@RequestBody EffectifDTO effectifDTO) throws URISyntaxException {
        LOG.debug("REST request to save Effectif : {}", effectifDTO);
        if (effectifDTO.getId() != null) {
            throw new BadRequestAlertException("A new effectif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        effectifDTO = effectifService.save(effectifDTO);
        return ResponseEntity.created(new URI("/api/effectifs/" + effectifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, effectifDTO.getId().toString()))
            .body(effectifDTO);
    }

    /**
     * {@code PUT  /effectifs/:id} : Updates an existing effectif.
     *
     * @param id the id of the effectifDTO to save.
     * @param effectifDTO the effectifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated effectifDTO,
     * or with status {@code 400 (Bad Request)} if the effectifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the effectifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EffectifDTO> updateEffectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EffectifDTO effectifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Effectif : {}, {}", id, effectifDTO);
        if (effectifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, effectifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!effectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        effectifDTO = effectifService.update(effectifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, effectifDTO.getId().toString()))
            .body(effectifDTO);
    }

    /**
     * {@code PATCH  /effectifs/:id} : Partial updates given fields of an existing effectif, field will ignore if it is null
     *
     * @param id the id of the effectifDTO to save.
     * @param effectifDTO the effectifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated effectifDTO,
     * or with status {@code 400 (Bad Request)} if the effectifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the effectifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the effectifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EffectifDTO> partialUpdateEffectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EffectifDTO effectifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Effectif partially : {}, {}", id, effectifDTO);
        if (effectifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, effectifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!effectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EffectifDTO> result = effectifService.partialUpdate(effectifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, effectifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /effectifs} : get all the effectifs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of effectifs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EffectifDTO>> getAllEffectifs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Effectifs");
        Page<EffectifDTO> page = effectifService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /effectifs/:id} : get the "id" effectif.
     *
     * @param id the id of the effectifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the effectifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EffectifDTO> getEffectif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Effectif : {}", id);
        Optional<EffectifDTO> effectifDTO = effectifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(effectifDTO);
    }

    /**
     * {@code DELETE  /effectifs/:id} : delete the "id" effectif.
     *
     * @param id the id of the effectifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEffectif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Effectif : {}", id);
        effectifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
