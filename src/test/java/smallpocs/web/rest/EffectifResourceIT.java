package smallpocs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static smallpocs.domain.EffectifAsserts.*;
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
import smallpocs.domain.Effectif;
import smallpocs.repository.EffectifRepository;
import smallpocs.service.dto.EffectifDTO;
import smallpocs.service.mapper.EffectifMapper;

/**
 * Integration tests for the {@link EffectifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EffectifResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CUMUL = 1L;
    private static final Long UPDATED_CUMUL = 2L;

    private static final String ENTITY_API_URL = "/api/effectifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EffectifRepository effectifRepository;

    @Autowired
    private EffectifMapper effectifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEffectifMockMvc;

    private Effectif effectif;

    private Effectif insertedEffectif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Effectif createEntity() {
        return new Effectif().name(DEFAULT_NAME).cumul(DEFAULT_CUMUL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Effectif createUpdatedEntity() {
        return new Effectif().name(UPDATED_NAME).cumul(UPDATED_CUMUL);
    }

    @BeforeEach
    public void initTest() {
        effectif = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEffectif != null) {
            effectifRepository.delete(insertedEffectif);
            insertedEffectif = null;
        }
    }

    @Test
    @Transactional
    void createEffectif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);
        var returnedEffectifDTO = om.readValue(
            restEffectifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(effectifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EffectifDTO.class
        );

        // Validate the Effectif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEffectif = effectifMapper.toEntity(returnedEffectifDTO);
        assertEffectifUpdatableFieldsEquals(returnedEffectif, getPersistedEffectif(returnedEffectif));

        insertedEffectif = returnedEffectif;
    }

    @Test
    @Transactional
    void createEffectifWithExistingId() throws Exception {
        // Create the Effectif with an existing ID
        effectif.setId(1L);
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEffectifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(effectifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEffectifs() throws Exception {
        // Initialize the database
        insertedEffectif = effectifRepository.saveAndFlush(effectif);

        // Get all the effectifList
        restEffectifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(effectif.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cumul").value(hasItem(DEFAULT_CUMUL.intValue())));
    }

    @Test
    @Transactional
    void getEffectif() throws Exception {
        // Initialize the database
        insertedEffectif = effectifRepository.saveAndFlush(effectif);

        // Get the effectif
        restEffectifMockMvc
            .perform(get(ENTITY_API_URL_ID, effectif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(effectif.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cumul").value(DEFAULT_CUMUL.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEffectif() throws Exception {
        // Get the effectif
        restEffectifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEffectif() throws Exception {
        // Initialize the database
        insertedEffectif = effectifRepository.saveAndFlush(effectif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the effectif
        Effectif updatedEffectif = effectifRepository.findById(effectif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEffectif are not directly saved in db
        em.detach(updatedEffectif);
        updatedEffectif.name(UPDATED_NAME).cumul(UPDATED_CUMUL);
        EffectifDTO effectifDTO = effectifMapper.toDto(updatedEffectif);

        restEffectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, effectifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(effectifDTO))
            )
            .andExpect(status().isOk());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEffectifToMatchAllProperties(updatedEffectif);
    }

    @Test
    @Transactional
    void putNonExistingEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        effectif.setId(longCount.incrementAndGet());

        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEffectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, effectifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(effectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        effectif.setId(longCount.incrementAndGet());

        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffectifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(effectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        effectif.setId(longCount.incrementAndGet());

        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffectifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(effectifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEffectifWithPatch() throws Exception {
        // Initialize the database
        insertedEffectif = effectifRepository.saveAndFlush(effectif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the effectif using partial update
        Effectif partialUpdatedEffectif = new Effectif();
        partialUpdatedEffectif.setId(effectif.getId());

        partialUpdatedEffectif.name(UPDATED_NAME).cumul(UPDATED_CUMUL);

        restEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEffectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEffectif))
            )
            .andExpect(status().isOk());

        // Validate the Effectif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEffectifUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEffectif, effectif), getPersistedEffectif(effectif));
    }

    @Test
    @Transactional
    void fullUpdateEffectifWithPatch() throws Exception {
        // Initialize the database
        insertedEffectif = effectifRepository.saveAndFlush(effectif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the effectif using partial update
        Effectif partialUpdatedEffectif = new Effectif();
        partialUpdatedEffectif.setId(effectif.getId());

        partialUpdatedEffectif.name(UPDATED_NAME).cumul(UPDATED_CUMUL);

        restEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEffectif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEffectif))
            )
            .andExpect(status().isOk());

        // Validate the Effectif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEffectifUpdatableFieldsEquals(partialUpdatedEffectif, getPersistedEffectif(partialUpdatedEffectif));
    }

    @Test
    @Transactional
    void patchNonExistingEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        effectif.setId(longCount.incrementAndGet());

        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, effectifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(effectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        effectif.setId(longCount.incrementAndGet());

        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffectifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(effectifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEffectif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        effectif.setId(longCount.incrementAndGet());

        // Create the Effectif
        EffectifDTO effectifDTO = effectifMapper.toDto(effectif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEffectifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(effectifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Effectif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEffectif() throws Exception {
        // Initialize the database
        insertedEffectif = effectifRepository.saveAndFlush(effectif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the effectif
        restEffectifMockMvc
            .perform(delete(ENTITY_API_URL_ID, effectif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return effectifRepository.count();
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

    protected Effectif getPersistedEffectif(Effectif effectif) {
        return effectifRepository.findById(effectif.getId()).orElseThrow();
    }

    protected void assertPersistedEffectifToMatchAllProperties(Effectif expectedEffectif) {
        assertEffectifAllPropertiesEquals(expectedEffectif, getPersistedEffectif(expectedEffectif));
    }

    protected void assertPersistedEffectifToMatchUpdatableProperties(Effectif expectedEffectif) {
        assertEffectifAllUpdatablePropertiesEquals(expectedEffectif, getPersistedEffectif(expectedEffectif));
    }
}
