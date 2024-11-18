package smallpocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static smallpocs.domain.ChampEffectifAsserts.*;
import static smallpocs.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import smallpocs.IntegrationTest;
import smallpocs.domain.ChampEffectif;
import smallpocs.repository.ChampEffectifRepository;
import smallpocs.service.dto.ChampEffectifDTO;
import smallpocs.service.mapper.ChampEffectifMapper;

/**
 * Integration tests for the {@link ChampEffectifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChampEffectifResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Long DEFAULT_VALEUR = 1L;
    private static final Long UPDATED_VALEUR = 2L;

    private static final String ENTITY_API_URL = "/api/champ-effectifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChampEffectifRepository champEffectifRepository;

    @Autowired
    private ChampEffectifMapper champEffectifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChampEffectifMockMvc;

    private ChampEffectif champEffectif;

    private ChampEffectif insertedChampEffectif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChampEffectif createEntity() {
        return new ChampEffectif().nom(DEFAULT_NOM).valeur(DEFAULT_VALEUR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChampEffectif createUpdatedEntity() {
        return new ChampEffectif().nom(UPDATED_NOM).valeur(UPDATED_VALEUR);
    }

    @BeforeEach
    public void initTest() {
        champEffectif = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedChampEffectif != null) {
            champEffectifRepository.delete(insertedChampEffectif);
            insertedChampEffectif = null;
        }
    }

    @Test
    @Transactional
    void createChampEffectif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);
        var returnedChampEffectifDTO = om.readValue(
            restChampEffectifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(champEffectifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ChampEffectifDTO.class
        );

        // Validate the ChampEffectif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedChampEffectif = champEffectifMapper.toEntity(returnedChampEffectifDTO);
        assertChampEffectifUpdatableFieldsEquals(returnedChampEffectif, getPersistedChampEffectif(returnedChampEffectif));

        insertedChampEffectif = returnedChampEffectif;
    }

    @Test
    @Transactional
    void createChampEffectifWithExistingId() throws Exception {
        // Create the ChampEffectif with an existing ID
        champEffectif.setId(1L);
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChampEffectifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(champEffectifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChampEffectifs() throws Exception {
        // Initialize the database
        insertedChampEffectif = champEffectifRepository.saveAndFlush(champEffectif);

        // Get all the champEffectifList
        restChampEffectifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(champEffectif.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR.intValue())));
    }

    @Test
    @Transactional
    void getChampEffectif() throws Exception {
        // Initialize the database
        insertedChampEffectif = champEffectifRepository.saveAndFlush(champEffectif);

        // Get the champEffectif
        restChampEffectifMockMvc
            .perform(get(ENTITY_API_URL_ID, champEffectif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(champEffectif.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingChampEffectif() throws Exception {
        // Get the champEffectif
        restChampEffectifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChampEffectif() throws Exception {
        // Initialize the database
        insertedChampEffectif = champEffectifRepository.saveAndFlush(champEffectif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the champEffectif
        ChampEffectif updatedChampEffectif = champEffectifRepository.findById(champEffectif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChampEffectif are not directly saved in db
        em.detach(updatedChampEffectif);
        updatedChampEffectif.nom(UPDATED_NOM).valeur(UPDATED_VALEUR);
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(updatedChampEffectif);

        restChampEffectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, champEffectifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(champEffectifDTO))
            )
            .andExpect(status().isOk());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChampEffectifToMatchAllProperties(updatedChampEffectif);
    }

    @Test
    @Transactional
    void putNonExistingChampEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        champEffectif.setId(longCount.incrementAndGet());

        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChampEffectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, champEffectifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(champEffectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChampEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        champEffectif.setId(longCount.incrementAndGet());

        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChampEffectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(champEffectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChampEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        champEffectif.setId(longCount.incrementAndGet());

        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChampEffectifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(champEffectifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChampEffectifWithPatch() throws Exception {
        // Initialize the database
        insertedChampEffectif = champEffectifRepository.saveAndFlush(champEffectif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the champEffectif using partial update
        ChampEffectif partialUpdatedChampEffectif = new ChampEffectif();
        partialUpdatedChampEffectif.setId(champEffectif.getId());

        partialUpdatedChampEffectif.valeur(UPDATED_VALEUR);

        restChampEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChampEffectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChampEffectif))
            )
            .andExpect(status().isOk());

        // Validate the ChampEffectif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChampEffectifUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedChampEffectif, champEffectif),
            getPersistedChampEffectif(champEffectif)
        );
    }

    @Test
    @Transactional
    void fullUpdateChampEffectifWithPatch() throws Exception {
        // Initialize the database
        insertedChampEffectif = champEffectifRepository.saveAndFlush(champEffectif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the champEffectif using partial update
        ChampEffectif partialUpdatedChampEffectif = new ChampEffectif();
        partialUpdatedChampEffectif.setId(champEffectif.getId());

        partialUpdatedChampEffectif.nom(UPDATED_NOM).valeur(UPDATED_VALEUR);

        restChampEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChampEffectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChampEffectif))
            )
            .andExpect(status().isOk());

        // Validate the ChampEffectif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChampEffectifUpdatableFieldsEquals(partialUpdatedChampEffectif, getPersistedChampEffectif(partialUpdatedChampEffectif));
    }

    @Test
    @Transactional
    void patchNonExistingChampEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        champEffectif.setId(longCount.incrementAndGet());

        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChampEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, champEffectifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(champEffectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChampEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        champEffectif.setId(longCount.incrementAndGet());

        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChampEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(champEffectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChampEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        champEffectif.setId(longCount.incrementAndGet());

        // Create the ChampEffectif
        ChampEffectifDTO champEffectifDTO = champEffectifMapper.toDto(champEffectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChampEffectifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(champEffectifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ChampEffectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChampEffectif() throws Exception {
        // Initialize the database
        insertedChampEffectif = champEffectifRepository.saveAndFlush(champEffectif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the champEffectif
        restChampEffectifMockMvc
            .perform(delete(ENTITY_API_URL_ID, champEffectif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return champEffectifRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ChampEffectif getPersistedChampEffectif(ChampEffectif champEffectif) {
        return champEffectifRepository.findById(champEffectif.getId()).orElseThrow();
    }

    protected void assertPersistedChampEffectifToMatchAllProperties(ChampEffectif expectedChampEffectif) {
        assertChampEffectifAllPropertiesEquals(expectedChampEffectif, getPersistedChampEffectif(expectedChampEffectif));
    }

    protected void assertPersistedChampEffectifToMatchUpdatableProperties(ChampEffectif expectedChampEffectif) {
        assertChampEffectifAllUpdatablePropertiesEquals(expectedChampEffectif, getPersistedChampEffectif(expectedChampEffectif));
    }
}
