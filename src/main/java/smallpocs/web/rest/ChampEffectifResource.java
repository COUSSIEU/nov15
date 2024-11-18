package smallpocs.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smallpocs.repository.ChampEffectifRepository;
import smallpocs.service.ChampEffectifService;
import smallpocs.service.dto.ChampEffectifDTO;
import smallpocs.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link smallpocs.domain.ChampEffectif}.
 */
@RestController
@RequestMapping("/api/champ-effectifs")
public class ChampEffectifResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChampEffectifResource.class);

    private static final String ENTITY_NAME = "champEffectif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChampEffectifService champEffectifService;

    private final ChampEffectifRepository champEffectifRepository;

    public ChampEffectifResource(ChampEffectifService champEffectifService, ChampEffectifRepository champEffectifRepository) {
        this.champEffectifService = champEffectifService;
        this.champEffectifRepository = champEffectifRepository;
    }

    /**
     * {@code POST  /champ-effectifs} : Create a new champEffectif.
     *
     * @param champEffectifDTO the champEffectifDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new champEffectifDTO, or with status {@code 400 (Bad Request)} if the champEffectif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChampEffectifDTO> createChampEffectif(@RequestBody ChampEffectifDTO champEffectifDTO) throws URISyntaxException {
        LOG.debug("REST request to save ChampEffectif : {}", champEffectifDTO);
        if (champEffectifDTO.getId() != null) {
            throw new BadRequestAlertException("A new champEffectif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        champEffectifDTO = champEffectifService.save(champEffectifDTO);
        return ResponseEntity.created(new URI("/api/champ-effectifs/" + champEffectifDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, champEffectifDTO.getId().toString()))
            .body(champEffectifDTO);
    }

    /**
     * {@code PUT  /champ-effectifs/:id} : Updates an existing champEffectif.
     *
     * @param id the id of the champEffectifDTO to save.
     * @param champEffectifDTO the champEffectifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated champEffectifDTO,
     * or with status {@code 400 (Bad Request)} if the champEffectifDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the champEffectifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChampEffectifDTO> updateChampEffectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChampEffectifDTO champEffectifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ChampEffectif : {}, {}", id, champEffectifDTO);
        if (champEffectifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, champEffectifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!champEffectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        champEffectifDTO = champEffectifService.update(champEffectifDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, champEffectifDTO.getId().toString()))
            .body(champEffectifDTO);
    }

    /**
     * {@code PATCH  /champ-effectifs/:id} : Partial updates given fields of an existing champEffectif, field will ignore if it is null
     *
     * @param id the id of the champEffectifDTO to save.
     * @param champEffectifDTO the champEffectifDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated champEffectifDTO,
     * or with status {@code 400 (Bad Request)} if the champEffectifDTO is not valid,
     * or with status {@code 404 (Not Found)} if the champEffectifDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the champEffectifDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChampEffectifDTO> partialUpdateChampEffectif(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ChampEffectifDTO champEffectifDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ChampEffectif partially : {}, {}", id, champEffectifDTO);
        if (champEffectifDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, champEffectifDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!champEffectifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChampEffectifDTO> result = champEffectifService.partialUpdate(champEffectifDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, champEffectifDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /champ-effectifs} : get all the champEffectifs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of champEffectifs in body.
     */
    @GetMapping("")
    public List<ChampEffectifDTO> getAllChampEffectifs() {
        LOG.debug("REST request to get all ChampEffectifs");
        return champEffectifService.findAll();
    }

    /**
     * {@code GET  /champ-effectifs/:id} : get the "id" champEffectif.
     *
     * @param id the id of the champEffectifDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the champEffectifDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChampEffectifDTO> getChampEffectif(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ChampEffectif : {}", id);
        Optional<ChampEffectifDTO> champEffectifDTO = champEffectifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(champEffectifDTO);
    }

    /**
     * {@code DELETE  /champ-effectifs/:id} : delete the "id" champEffectif.
     *
     * @param id the id of the champEffectifDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChampEffectif(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ChampEffectif : {}", id);
        champEffectifService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
